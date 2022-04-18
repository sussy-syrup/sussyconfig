package com.sussysyrup.sussyconfig;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigManager {

    File config;
    List<String> keys = new ArrayList<>();
    List<String> values = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    Logger LOGGER;

    public ConfigManager(String configName, Logger logger)
    {
        LOGGER = logger;
        
        Path path = FabricLoader.getInstance().getConfigDir();

        path = path.resolve(configName + ".config");

        config = path.toFile();

        FileInputStream stream = null;

        try {
            createConfig(config);
            stream = new FileInputStream(config);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<String> list = new ArrayList<>();

        String readString;
        try {
            while ((readString = reader.readLine()) != null) {
                if(!readString.contains("#")) {
                    list.add(readString);
                }
                else
                {
                    comments.add(readString.substring(1));
                }
            }
            reader.close();
        } catch (IOException e)
        {
            LOGGER.error(e.toString());
        }

        for(int i = 0; i < list.size(); i+=3)
        {
            if(i+3>list.size())
            {
                LOGGER.error("Config Broken, creating fresh file");
                try {
                    config.delete();
                    keys = new ArrayList<>();
                    values = new ArrayList<>();
                    createConfig(config);
                } catch (IOException e)
                {
                    LOGGER.error(e.toString());
                }

                break;
            }

            keys.add(list.get(i).substring(0, list.get(i).length() - 1));
            values.add(list.get(i+1));
        }
    }

    private static void createConfig(File config) throws IOException {
        if(!config.exists())
        {
            config.getParentFile().mkdirs();
            Files.createFile(config.toPath());
        }
    }

    public String getString(String key, String value)
    {
        int index;
        if(keys.contains(key))
        {
            index = keys.indexOf(key);
            return values.get(index);
        }
        else
        {
            try {
                keys.add(key);
                values.add(value);

                List<String> writeStrings = Arrays.asList(key + ":", value, "");
                Files.write(Path.of(config.getPath()), writeStrings, StandardOpenOption.APPEND);
            } catch (IOException e)
            {
                LOGGER.error("failed to write key: " + key);
            }

            return value;
        }
    }

    public Integer getInt(String key, int value)
    {
        int index;
        if(keys.contains(key))
        {
            try {
                index = keys.indexOf(key);
                return Integer.valueOf(values.get(index));
            } catch (NumberFormatException e)
            {
                LOGGER.error("Key " + key + " is not an integer, using default value");
                return value;
            }
        }
        else
        {
            try {
                keys.add(key);
                values.add(Integer.toString(value));

                List<String> writeStrings = Arrays.asList(key + ":", Integer.toString(value), "");
                Files.write(Path.of(config.getPath()), writeStrings, StandardOpenOption.APPEND);
            } catch (IOException e)
            {
                LOGGER.error("failed to write key: " + key);
            }

            return value;
        }
    }

    public Float getFloat(String key, float value)
    {
        int index;
        if(keys.contains(key))
        {
            try {
                index = keys.indexOf(key);
                return Float.valueOf(values.get(index));
            } catch (NumberFormatException e)
            {
                LOGGER.error("Key " + key + " is not a float, using default value");
                return value;
            }
        }
        else
        {
            try {
                keys.add(key);
                values.add(Float.toString(value));

                List<String> writeStrings = Arrays.asList(key + ":", Float.toString(value), "");
                Files.write(Path.of(config.getPath()), writeStrings, StandardOpenOption.APPEND);
            } catch (IOException e)
            {
                LOGGER.error("failed to write key: " + key);
            }

            return value;
        }
    }

    public Double getDouble(String key, double value)
    {
        int index;
        if(keys.contains(key))
        {
            try {
                index = keys.indexOf(key);
                return Double.valueOf(values.get(index));
            } catch (NumberFormatException e)
            {
                LOGGER.error("Key " + key + " is not a double, using default value");
                return value;
            }
        }
        else
        {
            try {
                keys.add(key);
                values.add(Double.toString(value));

                List<String> writeStrings = Arrays.asList(key + ":", Double.toString(value), "");
                Files.write(Path.of(config.getPath()), writeStrings, StandardOpenOption.APPEND);
            } catch (IOException e)
            {
                LOGGER.error("failed to write key: " + key);
            }

            return value;
        }
    }

    public Long getLong(String key, long value)
    {
        int index;
        if(keys.contains(key))
        {
            try {
                index = keys.indexOf(key);
                return Long.valueOf(values.get(index));
            } catch (NumberFormatException e)
            {
                LOGGER.error("Key " + key + " is not a long, using default value");
                return value;
            }
        }
        else
        {
            try {
                keys.add(key);
                values.add(Double.toString(value));

                List<String> writeStrings = Arrays.asList(key + ":", Long.toString(value), "");
                Files.write(Path.of(config.getPath()), writeStrings, StandardOpenOption.APPEND);
            } catch (IOException e)
            {
                LOGGER.error("failed to write key: " + key);
            }

            return value;
        }
    }

    public String[] getStringArray(String key, String[] value)
    {
        int index;
        if(keys.contains(key))
        {
            index = keys.indexOf(key);
            return values.get(index).split(",");
        }
        else
        {
            try {
                keys.add(key);
                values.add(String.join(",", value));

                List<String> writeStrings = Arrays.asList(key + ":", String.join(",", value), "");
                Files.write(Path.of(config.getPath()), writeStrings, StandardOpenOption.APPEND);
            } catch (IOException e)
            {
                LOGGER.error("failed to write key: " + key);
            }

            return value;
        }
    }

    public void writeComment(String comment)
    {
        try {
            if(!comments.contains(comment)) {
                Files.write(Path.of(config.getPath()), Collections.singleton("#" + comment), StandardOpenOption.APPEND);
            }
        } catch (IOException e)
        {
            LOGGER.error("failed to write comment: " + comment);
        }
    }


}
