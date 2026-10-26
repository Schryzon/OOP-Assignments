/**
 * XR900_Geopelia - Neucom's mass-produced autonomous superplane (Gepetto Project).
 * Omits human pilot accommodations entirely, relying on AI combat kernels (Nemo protocol).
 * Extends Combat_Aircraft and implements Autonomous_UAV.
 */
public class XR900_Geopelia extends Combat_Aircraft implements Autonomous_UAV {

    private String ai_kernel_version;
    private boolean autonomous_lock;

    public XR900_Geopelia(double max_speed, int payload_count){
        super("XR-900 Geopelia", Corporate_Faction.NEUCOM_INC, max_speed, payload_count);
        this.ai_kernel_version = "NEMO-KERNEL-v3.8";
        this.autonomous_lock = true;
    }

    @Override
    public void engage_ai_autopilot(String algorithm_profile){
        this.ai_kernel_version = algorithm_profile;
        this.autonomous_lock = true;
        System.out.println("[" + this.model_name + "] Autopilot engaged with profile: " + algorithm_profile);
    }

    @Override
    public boolean is_unmanned(){
        return this.autonomous_lock;
    }

    @Override
    public void execute_evasive_pattern(){
        System.out.println("[" + this.model_name + "] Executing unmanned super-maneuverability split-S (20G uninhibited turn)!");
    }

    @Override
    public double calculate_radar_cross_section(){
        // Compact windowless stealth airframe
        return 0.02;
    }

    @Override
    public void deploy_countermeasures(){
        System.out.println("[" + this.model_name + "] AI automated laser-defensive point matrix activated.");
    }

    @Override
    public void render_tactical_hud(){
        System.out.println("--------------------------------------------------");
        System.out.println("GEPETTO AUTONOMOUS TELEMETRY // " + this.model_name);
        System.out.println("Faction     : " + this.faction);
        System.out.println("Airspeed    : " + String.format("%.1f", this.current_speed) + " / " + this.max_speed + " km/h");
        System.out.println("Armor       : " + String.format("%.1f", this.armor_integrity) + "%");
        System.out.println("AI Profile  : " + this.ai_kernel_version);
        System.out.println("Unmanned    : " + is_unmanned());
        System.out.println("Munitions   : " + this.payload_count + " units ready");
        System.out.println("--------------------------------------------------");
    }
}
