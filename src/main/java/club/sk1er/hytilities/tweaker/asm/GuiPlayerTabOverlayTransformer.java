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
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

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

            if (methodName.equals("renderPlayerlist") || methodName.equals("func_175249_a")) {
                ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode next = iterator.next();

                    if (next instanceof MethodInsnNode && next.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        String methodInsnName = mapMethodNameFromNode(next);

                        // sort the player map and filter any entity with a uuid version of 2
                        if (methodInsnName.equals("getPlayerInfoMap") || methodInsnName.equals("func_175106_d")) {
                            method.instructions.insert(next, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                "club/sk1er/hytilities/handlers/lobby/npc/NPCHider",
                                "hideTabNpcs",
                                "(Ljava/util/Collection;)Ljava/util/Collection;",
                                false));
                            break;
                        }
                    }
                }
            } else if (methodName.equals("getPlayerName") || methodName.equals("func_175243_a")) {
                ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode next = iterator.next();

                    if (next instanceof MethodInsnNode && next.getOpcode() == Opcodes.INVOKESTATIC) {
                        String methodInsnName = mapMethodNameFromNode(next);

                        // trim the player name to remove player ranks and guild tags
                        if (methodInsnName.equals("formatPlayerName") || methodInsnName.equals("func_96667_a")) {
                            method.instructions.insert(next, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                "club/sk1er/hytilities/handlers/lobby/tab/TabNameChanger",
                                "modifyName",
                                "(Ljava/lang/String;)Ljava/lang/String;",
                                false));
                            break;
                        }
                    }
                }
            }
        }
    }
}
