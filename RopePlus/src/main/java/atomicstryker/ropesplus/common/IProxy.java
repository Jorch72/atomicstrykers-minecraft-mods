package atomicstryker.ropesplus.common;

import net.minecraftforge.common.config.Configuration;

public interface IProxy
{
    public void loadConfig(Configuration configFile);
    
    public void load();

    public boolean getShouldHookShotDisconnect();

    public void setShouldHookShotDisconnect(boolean b);
    
    /**
     * @return -1f to zipping in, 0f for 'no action', and any value above for extending the rope length
     */
    public float getShouldRopeChangeState();

    /**
     * @param f -1f to zipping in, 0f for 'no action', and any value above for extending the rope length
     */
    public void setShouldRopeChangeState(float f);

    public int getGrapplingHookRenderId();

    public boolean getHasClientRopeOut();

    public void setHasClientRopeOut(boolean b);
}
