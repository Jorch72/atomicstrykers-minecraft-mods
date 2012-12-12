package atomicstryker.simplyhax;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.relauncher.IClassTransformer;

/**
 * 
 * @author AtomicStryker
 * 
 * Magic happens in here. MAGIC.
 * Obfuscated names will have to be updated with each Obfuscation change.
 *
 */
public class SHTransformer implements IClassTransformer
{
    /* class net.minecraft.src.EntityPlayerSP */
    private final String classNamePlayerObfusc = "azv"; // 1.4.4 obfuscation
    
    private final String classNamePlayer = "net.minecraft.src.EntityPlayerSP";
    
    @Override
    public byte[] transform(String name, byte[] bytes)
    {
        //System.out.println("transforming: "+name);
        if (name.equals(classNamePlayerObfusc))
        {
            return handleTransform(bytes, true);
        }
        else if (name.equals(classNamePlayer))
        {
            return handleTransform(bytes, false);
        }
        
        return bytes;
    }
    
    private byte[] handleTransform(byte[] bytes, boolean obfuscated)
    {
        System.out.println("**************** Simply Hax transform running on EntityPlayerSP *********************** ");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        // find method to inject into
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
            MethodNode m = methods.next();
            if (m.desc.equals("(DDD)V")) // public void moveEntity(double x, double y, double z)
            {
                System.out.println("In target method! Patching!");
                
                AbstractInsnNode targetNode = null;
                Iterator iter = m.instructions.iterator();
                while (iter.hasNext())
                {
                    targetNode = (AbstractInsnNode) iter.next();
                    if (targetNode.getOpcode() == RETURN)
                    {
                        break;
                    }
                }
                                
                // make new instruction list
                InsnList toInject = new InsnList();
                
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "atomicstryker/simplyhax/SimplyHaxFlying", "preMoveEntityPlayerSP", "()V"));
                
                // inject new instruction list into method instruction list
                m.instructions.insertBefore(targetNode, toInject);
                
                System.out.println("Patching Complete!");
                break;
            }
        }
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}