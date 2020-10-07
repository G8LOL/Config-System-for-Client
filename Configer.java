package com.artemis.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.artemis.module.Module;
import com.artemis.Artemis;
import com.artemis.altmanager.AltLoginThread;
import com.artemis.logger.Logger;
import com.artemis.logger.Logger.LogType;
import com.artemis.settings.Setting;

import net.minecraft.client.Minecraft;


public class Configer {
	
	private File dir2;
	private File dir;
	private File dataFile;
	
	public static Configer instance = new Configer();
    private AltLoginThread thread;
	
	public void Config(String filename) {
		dir = new File(Minecraft.getMinecraft().mcDataDir, "Artemis Configs");
		if (!dir.exists()) {
			dir2.mkdir();
			Artemis.addChatMessage("Folder does not exist! Created new folder.");
			Logger.instance.log("Folder does not exist! Created new folder.", LogType.ERROR);
			return;
		}
		dataFile = new File(dir, filename + ".txt");
		
		if (!dataFile.exists()) {
			Artemis.addChatMessage("File does not exist!");
			Logger.instance.log("File does not exist!", LogType.ERROR);
			return;
		}
        this.load();
	
	}
	
	
	
	


	public void load() {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String s : lines) {
			String[] args = s.split(":");
			if (s.toLowerCase().startsWith("mod:")) {
				Module m = Artemis.instance.moduleManager.getModuleByName(args[1]);
				if (m != null) {
					m.setToggled(Boolean.parseBoolean(args[2]));
					m.setKey(Integer.parseInt(args[3]));
				}
			} else if (s.toLowerCase().startsWith("set:")) {
				Module m = Artemis.instance.moduleManager.getModuleByName(args[2]);
				if (m != null) {
					Setting set = Artemis.instance.settingsManager.getSettingByName(args[1]);
					if (set != null) {
						if (set.isCheck()) {
							set.setValBoolean(Boolean.parseBoolean(args[3]));
						}
						if (set.isCombo()) {
							set.setValString(args[3]);
						}
						if (set.isSlider()) {
							set.setValDouble(Double.parseDouble(args[3]));
						}
					}
				}
			}
		}
	}
	
}