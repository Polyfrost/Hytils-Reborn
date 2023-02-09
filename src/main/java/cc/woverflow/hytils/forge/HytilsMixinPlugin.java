/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.forge;


import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;

public class HytilsMixinPlugin implements IMixinConfigPlugin {

    private boolean isOptiFine = false;
    private boolean hasAppliedRenderHeads = false;
    private boolean hasAppliedModifyName = false;

    @Override
    public void onLoad(String mixinPackage) {
        try {
            Class.forName("net.optifine.render.RenderEnv");
            System.out.println("OptiFine detected, applying OptiFine compat mixin.");
            isOptiFine = true;
        } catch (ClassNotFoundException e) {
            System.out.println("OptiFine not detected, not applying OptiFine compat mixin.");
            isOptiFine = false;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("_OptiFine")) {
            return isOptiFine;
        } else if (mixinClassName.endsWith("_NoOptiFine")) {
            return !isOptiFine;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (!hasAppliedModifyName && !hasAppliedRenderHeads && targetClass != null && Objects.equals(targetClassName, "net.minecraft.client.gui.GuiPlayerTabOverlay")) {
            for (MethodNode method : targetClass.methods) {
                final String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, method.name, method.desc);
                final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
                switch (methodName) {
                    case "renderPlayerlist":
                    case "func_175249_a":
                        while (iterator.hasNext()) {
                            final AbstractInsnNode next = iterator.next();

                            if (next.getOpcode() == Opcodes.ILOAD && ((VarInsnNode) next).var == 11 && next.getNext().getOpcode() == Opcodes.IFEQ && !(next.getPrevious() instanceof VarInsnNode && ((VarInsnNode) next.getPrevious()).var == 10)) {
                                method.instructions.insert(next.getNext(), shouldRenderPlayerHead(((JumpInsnNode) next.getNext()).label));
                                hasAppliedRenderHeads = true;
                            }
                        }
                        break;
                    case "getPlayerName":
                    case "func_175243_a":
                        while (iterator.hasNext()) {
                            final AbstractInsnNode next = iterator.next();

                            if (next.getOpcode() == Opcodes.INVOKESTATIC) {
                                final String methodInsnName = mapMethodNameFromNode(next);

                                // trim the player name to remove player ranks and guild tags
                                if (methodInsnName.equals("formatPlayerName") || methodInsnName.equals("func_96667_a")) {
                                    method.instructions.insert(next, modifyName());
                                    hasAppliedModifyName = true;
                                    break;
                                }
                            }
                        }
                        break;
                }
            }
        }
    }

    private String mapMethodNameFromNode(AbstractInsnNode node) {
        MethodInsnNode methodInsnNode = (MethodInsnNode) node;
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);
    }

    private InsnList modifyName() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
            "cc/woverflow/hytils/handlers/lobby/tab/TabChanger",
            "modifyName",
            "(Ljava/lang/String;Lnet/minecraft/client/network/NetworkPlayerInfo;)Ljava/lang/String;",
            false));
        return list;
    }

    private InsnList shouldRenderPlayerHead(LabelNode label) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 24));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
            "cc/woverflow/hytils/handlers/lobby/tab/TabChanger",
            "shouldRenderPlayerHead",
            "(Lnet/minecraft/client/network/NetworkPlayerInfo;)Z",
            false));
        list.add(new JumpInsnNode(Opcodes.IFEQ, label));
        return list;
    }
}
