package com.github.tx.jsite.core.generate;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.github.tx.common.util.FileUtils;
import com.github.tx.common.util.JodaTimeUtil;
import com.github.tx.jsite.utils.FreeMarkers;
import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 根据模板生成代码
 * 
 * @author tangx
 * 
 */
public class Generate {

	private static Logger logger = LoggerFactory.getLogger(Generate.class);

	public static void main(String[] args) throws Exception {

		// ========== ↓↓↓↓↓↓ 执行前请修改参数，谨慎执行。↓↓↓↓↓↓ ====================

		/**
		 * 1、dao：
		 * {packageName}/{moduleName}/dao/{subModuleName}/{ClassName}Dao.java
		 * 
		 * 2、entity：
		 * {packageName}/{moduleName}/entity/{subModuleName}/{ClassName}.java
		 * 
		 * 3、service：
		 * {packageName}/{moduleName}/service/{subModuleName}/{ClassName}Service.java
		 * 
		 * 4、controller：
		 * {packageName}/{moduleName}/controller/{subModuleName}/{ClassName}Controller.java
		 * 
		 * 5、页面:
		 * {view}/{packageName}最后一个.后的字符串/{moduleName}/{subModuleName}/{className}List|Form.jsp 
		 * 
		 * 6、增删改权限标识：
		 * {moduleName}:{subModuleName}:{className}:create|edit|delete
		 * 注意：在代码生成后到菜单管理界面对增删改权限进行编辑否则无法看到按键
		 */

		// 以下参数必填
		String packageName = "com.github.tx.jsite.modules";// 生成代码所处包名
		String moduleName = "cms"; // 模块名，例：sys
		String className = "user"; // 功能英文名，例：user
		String functionName = "用户"; // 功能中文名，例：用户
		String tpl = "/src/main/resources/template";// 模板目录
		String view = "/src/main/webapp/WEB-INF/views";// 视图目录

		// 以下参数可选
		String subModuleName = ""; // 子模块名
		String tableName = "sys_user"; // 功能对应的数据库表名（若为空则默认与className一致）例如:sys_user
		String author = "tangx"; // 作者
		Boolean isPagination = true;// 列表页是否分页 true分页false不分页

		// ========== ↑↑↑↑↑↑ 执行前请修改参数，谨慎执行。↑↑↑↑↑↑ ====================

		if (StringUtils.isBlank(packageName) || StringUtils.isBlank(moduleName)
				|| StringUtils.isBlank(className) || StringUtils.isBlank(tpl)
				|| StringUtils.isBlank(view)
				|| StringUtils.isBlank(functionName)) {
			logger.error("参数设置错误：必填参数不能为空。");
			return;
		}

		// 获取文件分隔符
		String separator = File.separator;

		// 获取工程路径
		File projectPath = new DefaultResourceLoader().getResource("")
				.getFile();
		while (!new File(projectPath.getPath() + separator + "src" + separator
				+ "main").exists()) {
			projectPath = projectPath.getParentFile();
		}
		logger.info("Project Path: {}", projectPath);

		// 模板文件路径
		String tplPath = StringUtils.replace(projectPath + tpl, "/", separator);
		logger.info("Template Path: {}", tplPath);

		// 视图文件路径
		String viewPath = StringUtils.replace(projectPath + view, "/",
				separator);
		logger.info("View Path: {}", viewPath);

		// dao,service,controller,entity文件路径
		String javaPath = StringUtils.replaceEach(projectPath
				+ "/src/main/java/" + StringUtils.lowerCase(packageName),
				new String[] { "/", "." },
				new String[] { separator, separator });
		logger.info("Java Path: {}", javaPath);

		// 代码模板配置
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(tplPath));

		// 定义模板变量
		tableName = StringUtils.isNotBlank(tableName) ? StringUtils
				.lowerCase(tableName) : StringUtils.lowerCase(moduleName);
		Map<String, Object> model = Maps.newHashMap();
		model.put("packageName", StringUtils.lowerCase(packageName));
		// model.put("entityPackageName",
		// StringUtils.lowerCase(entityPackageName));
		model.put("tableName", tableName);
		model.put("moduleName", StringUtils.lowerCase(moduleName));
		model.put("subModuleName", StringUtils.isNotBlank(subModuleName) ? "."
				+ StringUtils.lowerCase(subModuleName) : "");
		model.put("className", StringUtils.uncapitalize(className));
		model.put("ClassName", StringUtils.capitalize(className));
		model.put("classAuthor", StringUtils.isNotBlank(author) ? author
				: "Generate Tools");
		model.put("classVersion", JodaTimeUtil.getCurrentDate());
		model.put("functionName", functionName);
		model.put(
				"urlPrefix",
				model.get("moduleName")
						+ (StringUtils.isNotBlank(subModuleName) ? "/"
								+ StringUtils.lowerCase(subModuleName) : "")
						+ "/" + model.get("className"));
		model.put("viewPrefix", // StringUtils.substringAfterLast(model.get("packageName"),".")+"/"+
				model.get("urlPrefix"));
		model.put(
				"permissionPrefix",
				model.get("moduleName")
						+ (StringUtils.isNotBlank(subModuleName) ? ":"
								+ StringUtils.lowerCase(subModuleName) : "")
						+ ":" + model.get("className"));
		model.put("entityFields", GenUtil.getRsmd(tableName));
		model.put("isPagination", isPagination);

		// 生成 Entity
		Template template = cfg.getTemplate("entity.ftl");
		String content = FreeMarkers.renderTemplate(template, model);
		String filePath = javaPath + separator + model.get("moduleName")
				+ separator + "entity" + separator
				+ StringUtils.lowerCase(subModuleName) + separator
				+ model.get("ClassName") + ".java";
		FileUtils.writeFile(content, filePath);
		logger.info("Entity: {}", filePath);

		// 生成 Dao
		template = cfg.getTemplate("dao.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = javaPath + separator + model.get("moduleName") + separator
				+ "dao" + separator + StringUtils.lowerCase(subModuleName)
				+ separator + model.get("ClassName") + "Dao.java";
		FileUtils.writeFile(content, filePath);
		logger.info("Dao: {}", filePath);

		// 生成 Service
		template = cfg.getTemplate("service.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = javaPath + separator + model.get("moduleName") + separator
				+ "service" + separator + StringUtils.lowerCase(subModuleName)
				+ separator + model.get("ClassName") + "Service.java";
		FileUtils.writeFile(content, filePath);
		logger.info("Service: {}", filePath);

		// 生成 Controller
		template = cfg.getTemplate("controller.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = javaPath + separator + model.get("moduleName") + separator
				+ "controller" + separator
				+ StringUtils.lowerCase(subModuleName) + separator
				+ model.get("ClassName") + "Controller.java";
		FileUtils.writeFile(content, filePath);
		logger.info("Controller: {}", filePath);

		// 生成 表单页
		template = cfg.getTemplate("viewForm.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = viewPath
				+ separator
				+ StringUtils.substringAfterLast(model.get("packageName")
						.toString(), ".") + separator + model.get("moduleName")
				+ separator + StringUtils.lowerCase(subModuleName) + separator
				+ model.get("className") + "Form.jsp";
		FileUtils.writeFile(content, filePath);
		logger.info("ViewForm: {}", filePath);

		// 生成列表页
		template = cfg.getTemplate("viewList.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = viewPath
				+ separator
				+ StringUtils.substringAfterLast(model.get("packageName")
						.toString(), ".") + separator + model.get("moduleName")
				+ separator + StringUtils.lowerCase(subModuleName) + separator
				+ model.get("className") + "List.jsp";
		FileUtils.writeFile(content, filePath);
		logger.info("ViewList: {}", filePath);

		logger.info("Generate Success.");
	}
}
