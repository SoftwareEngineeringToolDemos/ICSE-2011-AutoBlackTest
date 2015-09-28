/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.Buddi;
import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.plugin.api.BuddiExportPlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiImportPlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiPanelPlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiRunnablePlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiSynchronizePlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiTransactionCellRendererPlugin;

import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.application.plugin.factory.PluginFactory;
import ca.digitalcave.moss.common.ClassLoaderFunctions;

public class BuddiPluginFactory extends PluginFactory {
	
	private static final Map<Class<? extends MossPlugin>, List<MossPlugin>> pluginMap = new HashMap<Class<? extends MossPlugin>, List<MossPlugin>>();

	/**
	 * Removes all the plugins from the cache.  Next time the plugins are
	 * accessed, we will reload from disk.
	 */
	public static void forcePluginRefresh(){
		pluginMap.clear();
	}
	
	/**
	 * Returns all plugins, both in the classpath and in plugin dirs, of the given type.
	 * @param pluginType
	 * @return
	 */
	public static List<? extends MossPlugin> getPlugins(Class<? extends MossPlugin> pluginType){
		if (pluginMap.get(pluginType) == null){
			pluginMap.put(pluginType, new LinkedList<MossPlugin>());
			
			//Load built in plugins
			String[] builtIn;
			if (pluginType.getName().equals(BuddiReportPlugin.class.getName()))
				builtIn = Const.BUILT_IN_REPORTS;
			else if (pluginType.getName().equals(BuddiImportPlugin.class.getName()))
				builtIn = Const.BUILT_IN_IMPORTS;
			else if (pluginType.getName().equals(BuddiExportPlugin.class.getName()))
				builtIn = Const.BUILT_IN_EXPORTS;
			else if (pluginType.getName().equals(BuddiPreferencePlugin.class.getName()))
				builtIn = Const.BUILT_IN_PREFERENCE_PANELS;
			else if (pluginType.getName().equals(BuddiRunnablePlugin.class.getName()))
				builtIn = Const.BUILT_IN_RUNNABLES;
			else if (pluginType.getName().equals(BuddiSynchronizePlugin.class.getName()))
				builtIn = Const.BUILT_IN_SYNCHRONIZES;
			else if (pluginType.getName().equals(BuddiTransactionCellRendererPlugin.class.getName()))
				builtIn = Const.BUILT_IN_TRANSACTION_CELL_RENDERERS;
			else if (pluginType.getName().equals(BuddiPanelPlugin.class.getName()))
				builtIn = Const.BUILT_IN_PANELS;
			else {
				builtIn = new String[0];
				Logger.getLogger(BuddiPluginFactory.class.getName()).warning("Unknown plugin type: " + pluginType.getName());
			}
			
			for (String className : builtIn){
				MossPlugin plugin = BuddiPluginFactory.getValidPluginFromClasspath(className);
				if (pluginType.isInstance(plugin)){
					pluginMap.get(pluginType).add(plugin);
				}
			}

			//Load user defined plugins
			File[] plugins = Buddi.getPluginsFolder().listFiles(pluginFilter); 
			if (plugins != null){
				for (File pluginFile : plugins){
					Properties props = new Properties();
					try {
						InputStream is = ClassLoaderFunctions.getResourceAsStreamFromJar(pluginFile, "/" + Const.PLUGIN_PROPERTIES);
						if (is != null)
							props.load(is);
					}
					catch (IOException ioe){}
					for (MossPlugin plugin : getMossPluginsFromJar(pluginFile, Buddi.getVersion(), props.getProperty(Const.PLUGIN_PROPERTIES_ROOT))) {
						if (pluginType.isInstance(plugin)){
							pluginMap.get(pluginType).add((MossPlugin) plugin);
						}
					}
				}
			}
		}
		
		return pluginMap.get(pluginType);
	}
	
	public static final FileFilter pluginFilter = new FileFilter(){
		public boolean accept(File pathname) {
			if (pathname.getName().endsWith(Const.PLUGIN_EXTENSION)
					&& pathname.isFile())
				return true;
			return false;
		}
	};
	
	public static List<File> getPluginFiles(){
		List<File> pluginFiles = new LinkedList<File>();
		
		//Loop through all files fuond in the plugins folder, and see if they contain a valid
		// plugin.  If so, add it to the list.  This time (Buddi 3), we focus on the plugin file
		// itself, rather than each plugin class within it.
		File[] plugins = Buddi.getPluginsFolder().listFiles(pluginFilter); 
		if (plugins != null){
			for (File pluginFile : plugins){
				Properties props = new Properties();
				try {
					InputStream is = ClassLoaderFunctions.getResourceAsStreamFromJar(pluginFile, "/" + Const.PLUGIN_PROPERTIES);
					if (is != null)
						props.load(is);
				}
				catch (IOException ioe){}
				for (MossPlugin plugin : getMossPluginsFromJar(pluginFile, Buddi.getVersion(), props.getProperty(Const.PLUGIN_PROPERTIES_ROOT))) {
					if (plugin instanceof BuddiExportPlugin
							|| plugin instanceof BuddiImportPlugin
							|| plugin instanceof BuddiSynchronizePlugin
							|| plugin instanceof BuddiPreferencePlugin
							|| plugin instanceof BuddiReportPlugin
							|| plugin instanceof BuddiRunnablePlugin
							|| plugin instanceof BuddiTransactionCellRendererPlugin
							|| plugin instanceof BuddiPanelPlugin){
						pluginFiles.add(pluginFile);
						break;
					}
				}
			}
		}
		
		return pluginFiles;
	}
}
