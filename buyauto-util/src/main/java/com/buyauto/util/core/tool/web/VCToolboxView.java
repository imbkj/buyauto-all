/**
 * 
 */
package com.buyauto.util.core.tool.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

/**
 * 兼容toolbar 2.0插件
 * 
 * @author Administrator
 * 
 */
public class VCToolboxView extends VelocityToolboxView {

	@Override
	protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ViewToolContext ctx;

		ctx = new ViewToolContext(this.getVelocityEngine(), request, response, this.getServletContext());

		ctx.putAll(model);
		if (this.getToolboxConfigLocation() != null) {
			ToolManager tm = new ToolManager();
			tm.setVelocityEngine(this.getVelocityEngine());
			tm.configure(this.getServletContext().getRealPath(this.getToolboxConfigLocation()));
			if (tm.getToolboxFactory().hasTools(Scope.REQUEST)) {
				ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.REQUEST));
			}
			if (tm.getToolboxFactory().hasTools(Scope.APPLICATION)) {
				ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.APPLICATION));
			}
			if (tm.getToolboxFactory().hasTools(Scope.SESSION)) {
				ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.SESSION));
			}
		}
		return ctx;

	}

}
