package com.activiti.model;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.activiti.model.dto.ModelDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.Lists;
import com.common.ServerResponse;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 流程模型控制器
 *
 */
@Controller
@RequestMapping(value = "/model")
public class ModelController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 查询模型列表
	 */
	@RequestMapping(value = "/list")
	public @ResponseBody ServerResponse<List<ModelDTO>> modelList() {
		List<Model> modelList = repositoryService.createModelQuery().list();
		List<ModelDTO> modelDTOList = Lists.newArrayList();
		for(Model model : modelList){
			ModelDTO modelDTO = new ModelDTO();
			modelDTO.setKey(model.getKey());
			modelDTO.setName(model.getName());
			modelDTO.setCreateTime(model.getCreateTime());
			modelDTO.setLastUpdateTime(model.getLastUpdateTime());
			modelDTO.setDesc(model.getMetaInfo());
			modelDTO.setModelId(model.getId());
			modelDTOList.add(modelDTO);

		}
		return ServerResponse.buildSuccessData(modelDTOList);
	}

	/**
	 * 创建模型
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody ServerResponse<String> create(@RequestParam("name") String name
			, @RequestParam("key") String key, @RequestParam("description") String description
			, HttpServletRequest request) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			return ServerResponse.buildSuccessData(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
		} catch (Exception e) {
			return ServerResponse.buildErrorMsg("创建模型失败!");
		}
	}

	/**
	 * 根据 Model部署流程定义
	 */
	@RequestMapping(value = "/deploy/{modelId}")
	public @ResponseBody ServerResponse<String> deploy(@PathVariable("modelId") String modelId,
			RedirectAttributes redirectAttributes) {
		try {
			// 获取流程模型
			Model modelData = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);

			// 把流程模型转为字节数组
			byte[] bpmnBytes = null;
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);

			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addString(processName, new String(bpmnBytes)).deploy();

			return ServerResponse.buildSuccessMsg("部署成功，部署ID=" + deployment.getId());
		} catch (Exception e) {
			logger.error("根据模型部署流程失败：modelId={}", modelId, e);
			return ServerResponse.buildErrorMsg("根据模型部署流程失败：modelId=" + modelId);
		}
	}

	/**
	 * 删除模型
	 */
	@RequestMapping(value = "/delete/{modelId}")
	public  @ResponseBody ServerResponse<String> delete(@PathVariable("modelId") String modelId) {
		try {
			repositoryService.deleteModel(modelId);
			return ServerResponse.buildSuccessMsg("删除成功!");
		} catch (Exception e) {
			return ServerResponse.buildErrorMsg("删除失败!");
		}
	}
	
	/**
	 * 导出model对象为 bpmn 类型
	 *
	 * @param modelId 模型ID
	 *
	 */
	@RequestMapping(value = "/export/{modelId}")
	public void export(@PathVariable("modelId") String modelId, HttpServletResponse response) {
		try {
			Model modelData = repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

			JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			// 处理异常
			if (bpmnModel.getMainProcess() == null) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				response.getOutputStream().println("no main process, can't export");
				response.flushBuffer();
				return;
			}
			String filename = "";
			byte[] exportBytes = null;
			String mainProcessId = bpmnModel.getMainProcess().getId();
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			exportBytes = xmlConverter.convertToXML(bpmnModel);
			filename = mainProcessId + ".bpmn20.xml";



			ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
			IOUtils.copy(in, response.getOutputStream());
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("导出model的xml文件失败：modelId={}", modelId, e);
		}
	}

	

}
