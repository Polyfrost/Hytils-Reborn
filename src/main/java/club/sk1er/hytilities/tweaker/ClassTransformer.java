package club.sk1er.hytilities.tweaker;

import club.sk1er.hytilities.tweaker.asm.GuiPlayerTabOverlayTransformer;
import club.sk1er.hytilities.tweaker.asm.LayerArmorBaseTweaker;
import club.sk1er.hytilities.tweaker.asm.MinecraftTransformer;
import club.sk1er.hytilities.tweaker.transformer.HytilitiesTransformer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class ClassTransformer implements IClassTransformer {

    private final Multimap<String, HytilitiesTransformer> transformerMap = ArrayListMultimap.create();
    private final boolean outputBytecode = Boolean.parseBoolean(System.getProperty("debugBytecode", "false"));

    public ClassTransformer() {
        registerTransformer(new MinecraftTransformer());
        registerTransformer(new GuiPlayerTabOverlayTransformer());
        registerTransformer(new LayerArmorBaseTweaker());
    }

    private void registerTransformer(HytilitiesTransformer transformer) {
        for (String cls : transformer.getClassName()) {
            transformerMap.put(cls, transformer);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        Collection<HytilitiesTransformer> transformers = transformerMap.get(transformedName);
        if (transformers.isEmpty()) return bytes;

        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        for (HytilitiesTransformer transformer : transformers) {
            transformer.transform(classNode, transformedName);
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        try {
            classNode.accept(classWriter);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (outputBytecode) {
            File bytecodeDirectory = new File("bytecode");
            String transformedClassName;

            // anonymous classes
            if (transformedName.contains("$")) {
                transformedClassName = transformedName.replace('$', '.') + ".class";
            } else {
                transformedClassName = transformedName + ".class";
            }

            File bytecodeOutput = new File(bytecodeDirectory, transformedClassName);

            try {
                if (!bytecodeDirectory.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    bytecodeDirectory.mkdirs();
                }

                if (!bytecodeOutput.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    bytecodeOutput.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try (FileOutputStream os = new FileOutputStream(bytecodeOutput)) {
                os.write(classWriter.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return classWriter.toByteArray();
    }
}
