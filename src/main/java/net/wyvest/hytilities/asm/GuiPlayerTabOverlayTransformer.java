/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.asm;

import net.wyvest.hytilities.forge.transformer.HytilitiesTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

//TODO: implement this as a mixin apparently mixin 0.8 has custom injector things or something so use that maybe
public class GuiPlayerTabOverlayTransformer implements HytilitiesTransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.gui.GuiPlayerTabOverlay"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            final String methodName = mapMethodName(classNode, method);
            final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
            switch (methodName) {
                case "renderPlayerlist":
                case "func_175249_a":
                    while (iterator.hasNext()) {
                        final AbstractInsnNode next = iterator.next();

                        if (next.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            final String methodInsnName = mapMethodNameFromNode(next);

                            // sort the player map and filter any entity with a uuid version of 2
                            if (methodInsnName.equals("getPlayerInfoMap") || methodInsnName.equals("func_175106_d")) {
                                method.instructions.insert(next, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    "net/wyvest/hytilities/handlers/lobby/npc/NPCHandler",
                                    "hideTabNpcs",
                                    "(Ljava/util/Collection;)Ljava/util/Collection;",
                                    false));
                            }
                        } else if (next.getOpcode() == Opcodes.ILOAD && ((VarInsnNode) next).var == 11 && next.getNext().getOpcode() == Opcodes.IFEQ && !(next.getPrevious() instanceof VarInsnNode && ((VarInsnNode)next.getPrevious()).var == 10)) {
                            method.instructions.insert(next.getNext(), shouldRenderPlayerHead(((JumpInsnNode) next.getNext()).label));
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
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    }

    private InsnList modifyName() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
            "net/wyvest/hytilities/handlers/lobby/tab/TabChanger",
            "modifyName",
            "(Ljava/lang/String;Lnet/minecraft/client/network/NetworkPlayerInfo;)Ljava/lang/String;",
            false));
        return list;
    }

    // && TabChanger.shouldRenderPlayerHead(networkplayerinfo1)
    private InsnList shouldRenderPlayerHead(LabelNode label) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 24));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/wyvest/hytilities/handlers/lobby/tab/TabChanger", "shouldRenderPlayerHead", "(Lnet/minecraft/client/network/NetworkPlayerInfo;)Z", false));
        list.add(new JumpInsnNode(Opcodes.IFEQ, label));
        return list;
    }
}
