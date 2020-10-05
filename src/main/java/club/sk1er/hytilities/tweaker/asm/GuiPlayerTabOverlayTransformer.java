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

                break;
            } else if (methodName.equals("getPlayerName") || methodName.equals("func_175243_a")) {
                ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode next = iterator.next();

                    if (next instanceof MethodInsnNode && next.getOpcode() == Opcodes.INVOKESTATIC) {
                        String methodInsnName = mapMethodNameFromNode(next);

                        // trim the player name to remove guild tags
                        if (methodInsnName.equals("formatPlayerName") || methodInsnName.equals("func_96667_a")) {
                            method.instructions.insert(next, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    "club/sk1er/hytilities/handlers/lobby/tab/GuildTagHider",
                                    "hideTabGuildTags",
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
