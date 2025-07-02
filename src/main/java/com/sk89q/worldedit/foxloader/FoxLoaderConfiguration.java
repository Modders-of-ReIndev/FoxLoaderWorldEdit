package com.sk89q.worldedit.foxloader;

import com.sk89q.worldedit.util.PropertiesConfiguration;

import java.io.File;

public class FoxLoaderConfiguration extends PropertiesConfiguration {
    public FoxLoaderConfiguration(FoxLoaderWorldEdit mod) {
        super(new File(mod.getWorkingDir(), "worldedit.properties"));
    }

    @Override
    public File getWorkingDirectory() {
        return FoxLoaderWorldEdit.inst.getWorkingDir();
    }
}
