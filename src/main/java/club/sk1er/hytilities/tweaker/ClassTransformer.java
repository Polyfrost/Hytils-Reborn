/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.sk1er.hytilities.tweaker;

import club.sk1er.hytilities.tweaker.asm.EntityPlayerSPTransformer;
import club.sk1er.hytilities.tweaker.asm.GuiIngameForgeTransformer;
import club.sk1er.hytilities.tweaker.asm.GuiPlayerTabOverlayTransformer;
import club.sk1er.hytilities.tweaker.asm.LayerArmorBaseTransformer;
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
        registerTransformer(new LayerArmorBaseTransformer());
        registerTransformer(new GuiIngameForgeTransformer());
        registerTransformer(new EntityPlayerSPTransformer());
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
