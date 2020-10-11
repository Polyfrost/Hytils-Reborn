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

package club.sk1er.hytilities.tweaker.asm;

import club.sk1er.hytilities.tweaker.transformer.HytilitiesTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class GuiPlayerTabOverlayTransformer implements HytilitiesTransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.gui.GuiPlayerTabOverlay"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            String methodName = mapMethodName(classNode, method);
            ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();

            switch (methodName) {
                case "renderPlayerlist":
                case "func_175249_a":
                    while (iterator.hasNext()) {
                        AbstractInsnNode next = iterator.next();

                        if (next.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            String methodInsnName = mapMethodNameFromNode(next);

                            // sort the player map and filter any entity with a uuid version of 2
                            if (methodInsnName.equals("getPlayerInfoMap") || methodInsnName.equals("func_175106_d")) {
                                method.instructions.insert(next, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    "club/sk1er/hytilities/handlers/lobby/npc/NPCHider",
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
                        AbstractInsnNode next = iterator.next();

                        if (next.getOpcode() == Opcodes.INVOKESTATIC) {
                            String methodInsnName = mapMethodNameFromNode(next);

                            // trim the player name to remove player ranks and guild tags
                            if (methodInsnName.equals("formatPlayerName") || methodInsnName.equals("func_96667_a")) {
                                method.instructions.insert(next, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    "club/sk1er/hytilities/handlers/lobby/tab/TabChanger",
                                    "modifyName",
                                    "(Ljava/lang/String;)Ljava/lang/String;",
                                    false));
                                break;
                            }
                        }
                    }
                    break;

                case "drawPing":
                case "func_175245_a":
                    method.instructions.insert(hidePing());
                    break;
            }
        }
    }

    private InsnList shouldRenderPlayerHead(LabelNode label) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 24)); // networkplayerinfo1
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "club/sk1er/hytilities/handlers/lobby/tab/TabChanger", "shouldRenderPlayerHead", "(Lnet/minecraft/client/network/NetworkPlayerInfo;)Z", false));
        list.add(new JumpInsnNode(Opcodes.IFEQ, label));
        return list;
    }

    private InsnList hidePing() {
        InsnList list = new InsnList();
        LabelNode label = new LabelNode();
        list.add(new VarInsnNode(Opcodes.ALOAD, 4));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "club/sk1er/hytilities/handlers/lobby/tab/TabChanger", "hidePing", "(Lnet/minecraft/client/network/NetworkPlayerInfo;)Z", false));
        list.add(new JumpInsnNode(Opcodes.IFEQ, label));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(label);
        return list;
    }
}
