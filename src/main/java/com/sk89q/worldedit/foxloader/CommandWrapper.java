package com.sk89q.worldedit.foxloader;

import com.fox2code.foxloader.registry.CommandRegistry;
import com.google.common.base.Joiner;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.platform.CommandEvent;
import com.sk89q.worldedit.util.command.CommandMapping;
import net.minecraft.common.command.Command;
import net.minecraft.common.command.ICommandListener;
import net.minecraft.common.command.IllegalCmdListenerOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandWrapper extends Command {
    private static final HashMap<String, String[]> commandMap = new HashMap<>();
    private static final ArrayList<CommandWrapper> wrappers = new ArrayList<>();

    static {
        commandMap.put("size", new String[]{});
        commandMap.put("/wand", new String[]{});
        commandMap.put("floodfill", new String[]{"flood"});
        commandMap.put("/fill", new String[]{});
        commandMap.put("/sphere", new String[]{});
        commandMap.put(".s", new String[]{});
        commandMap.put("range", new String[]{});
        commandMap.put("lrbuild", new String[]{"/lrbuild"});
        commandMap.put("/help", new String[]{});
        commandMap.put("/calc", new String[]{"/calculate", "/eval", "/evaluate", "/solve"});
        commandMap.put("/hsphere", new String[]{});
        commandMap.put("/hpos2", new String[]{});
        commandMap.put("up", new String[]{});
        commandMap.put("/removenear", new String[]{"removenear"});
        commandMap.put("/move", new String[]{});
        commandMap.put("/undo", new String[]{"undo"});
        commandMap.put("thru", new String[]{"thru"});
        commandMap.put("/clearhistory", new String[]{"clearhistory"});
        commandMap.put("/cyl", new String[]{});
        commandMap.put("/removeabove", new String[]{"removeabove"});
        commandMap.put("tool", new String[]{});
        commandMap.put("/hollow", new String[]{});
        commandMap.put("mask", new String[]{});
        commandMap.put("/copy", new String[]{});
        commandMap.put("/paste", new String[]{});
        commandMap.put("cs", new String[]{});
        commandMap.put("/setbiome", new String[]{});
        commandMap.put("pumpkins", new String[]{});
        commandMap.put("biomelist", new String[]{});
        commandMap.put("/removebelow", new String[]{"removebelow"});
        commandMap.put("info", new String[]{});
        commandMap.put("/drain", new String[]{});
        commandMap.put("/generatebiome", new String[]{});
        commandMap.put("/distr", new String[]{});
        commandMap.put("/generate", new String[]{});
        commandMap.put("/rotate", new String[]{});
        commandMap.put("ceil", new String[]{});
        commandMap.put("/forest", new String[]{});
        commandMap.put("/hpos1", new String[]{});
        commandMap.put("/inset", new String[]{});
        commandMap.put("descend", new String[]{"desc"});
        commandMap.put("/walls", new String[]{});
        commandMap.put("chunkinfo", new String[]{});
        commandMap.put("/faces", new String[]{});
        commandMap.put("/green", new String[]{});
        commandMap.put("/redo", new String[]{});
        commandMap.put("/pyramid", new String[]{});
        commandMap.put("schematic", new String[]{"schem", "/schematic", "/schem"});
        commandMap.put("/size", new String[]{});
        commandMap.put("/", new String[]{});
        commandMap.put("delchunks", new String[]{});
        commandMap.put("biomeinfo", new String[]{});
        commandMap.put("tree", new String[]{});
        commandMap.put("/count", new String[]{});
        commandMap.put("/flip", new String[]{});
        commandMap.put("/center", new String[]{});
        commandMap.put("/deform", new String[]{});
        commandMap.put("brush", new String[]{"br"});
        commandMap.put("/ex", new String[]{"/ext", "/extinguish", "ex", "ext", "extinguish"});
        commandMap.put("unstuck", new String[]{"!"});
        commandMap.put("snapshot", new String[]{"snap"});
        commandMap.put("/stack", new String[]{});
        commandMap.put("/replace", new String[]{"/re", "/rep"});
        commandMap.put("/load", new String[]{});
        commandMap.put("/contract", new String[]{});
        commandMap.put("/pos2", new String[]{});
        commandMap.put("repl", new String[]{});
        commandMap.put("/fillr", new String[]{});
        commandMap.put("/outset", new String[]{});
        commandMap.put("deltree", new String[]{});
        commandMap.put("/replacenear", new String[]{"replacenear"});
        commandMap.put("restore", new String[]{"/restore"});
        commandMap.put("/expand", new String[]{});
        commandMap.put("ascend", new String[]{});
        commandMap.put("/save", new String[]{});
        commandMap.put("/smooth", new String[]{});
        commandMap.put("worldedit", new String[]{"we"});
        commandMap.put("none", new String[]{});
        commandMap.put("/line", new String[]{});
        commandMap.put("/hcyl", new String[]{});
        commandMap.put("/curve", new String[]{});
        commandMap.put("/sel", new String[]{";", "/desel", "/deselect"});
        commandMap.put("remove", new String[]{"rem", "rement"});
        commandMap.put("mat", new String[]{"material"});
        commandMap.put("/thaw", new String[]{"thaw"});
        commandMap.put("/fixlava", new String[]{"fixlava"});
        commandMap.put("/gmask", new String[]{"gmask"});
        commandMap.put("butcher", new String[]{});
        commandMap.put("superpickaxe", new String[]{"pickaxe", "sp"});
        commandMap.put("/naturalize", new String[]{});
        commandMap.put("jumpto", new String[]{"j"});
        commandMap.put("/shift", new String[]{});
        commandMap.put("/toggleplace", new String[]{"toggleplace"});
        commandMap.put("/cut", new String[]{});
        commandMap.put("clearclipboard", new String[]{});
        commandMap.put("cycler", new String[]{});
        commandMap.put("/set", new String[]{});
        commandMap.put("/overlay", new String[]{});
        commandMap.put("/limit", new String[]{});
        commandMap.put("/fixwater", new String[]{"fixwater"});
        commandMap.put("/snow", new String[]{"snow"});
        commandMap.put("/hpyramid", new String[]{});
        commandMap.put("toggleeditwand", new String[]{});
        commandMap.put("/searchitem", new String[]{"/l", "/search", "searchitem"});
        commandMap.put("farwand", new String[]{});
        commandMap.put("/chunk", new String[]{});
        commandMap.put("/pos1", new String[]{});
        commandMap.put("/regen", new String[]{});
        commandMap.put("forestgen", new String[]{});
        commandMap.put("/flora", new String[]{});
        commandMap.put("listchunks", new String[]{});
        for (Map.Entry<String, String[]> entry : commandMap.entrySet()) {
            wrappers.add(new CommandWrapper(entry));
        }
    }

    static void register() {
        wrappers.forEach(CommandRegistry::registerCommand);
    }

    static void debugMissing(CommandMapping commandMapping) {
        if (!commandMap.containsKey(commandMapping.getPrimaryAlias())) {
            FoxLoaderWorldEdit.inst.getLogger().warning(
                    "Missing command \"" + commandMapping.getPrimaryAlias() + "\"" +
                            " with aliases " + Arrays.toString(commandMapping.getAllAliases()));
        }
    }

    private CommandWrapper(Map.Entry<String, String[]> command) {
        super(command.getKey(), true, false, command.getValue());
    }

    @Override
    public void printHelpInformation(ICommandListener commandExecutor) {

    }

    @Override
    public void onExecute(String[] args, ICommandListener commandExecutor) throws IllegalCmdListenerOperation {
        WorldEdit.getInstance().getEventBus().post(new CommandEvent(
                new FoxLoaderPlayer(commandExecutor.getPlayerEntity()), Joiner.on(" ").join(args)));
    }

    @Override
    public String commandSyntax() {
        return "";
    }
}
