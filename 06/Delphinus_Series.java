/**
 * Delphinus_Series - Neucom Incorporated's advanced laminar-flow aerodyne series (R-101/R-102/R-103).
 * Features superior high-altitude aerodynamics and synthetic bio-composite airframe construction.
 */
public class Delphinus_Series extends Combat_Aircraft {

    private double laminar_flow_coefficient;

    public Delphinus_Series(String model_name, double max_speed, int payload_count, double laminar_flow_coefficient){
        super(model_name, Corporate_Faction.NEUCOM_INC, max_speed, payload_count);
        this.laminar_flow_coefficient = Math.max(0.1, Math.min(1.0, laminar_flow_coefficient));
    }

    @Override
    public boolean execute_maneuver(double g_force){
        // Neucom laminar aerodyne supports up to 14G via COFFIN G-dampening
        if(g_force <= 0.0){
            return false;
        }

        if(g_force > 14.0){
            System.out.println("[" + this.model_name + "] Bio-composite stress limit exceeded at " + g_force + "G!");
            take_damage(8.0);
            return false;
        }

        return true;
    }

    @Override
    public double calculate_radar_cross_section(){
        // Smooth carbon-resin skin reduces RCS significantly
        return 0.05 * (1.0 - (this.laminar_flow_coefficient * 0.4));
    }

    @Override
    public void deploy_countermeasures(){
        System.out.println("[" + this.model_name + "] Deploying electronic aero-shield dispersion chaff and nano-jammers.");
    }

    @Override
    public void render_tactical_hud(){
        System.out.println("--------------------------------------------------");
        System.out.println("NEUCOM ELECTROSPHERE HUD // " + this.model_name);
        System.out.println("Faction   : " + this.faction);
        System.out.println("Airspeed  : " + String.format("%.1f", this.current_speed) + " / " + this.max_speed + " km/h");
        System.out.println("Armor     : " + String.format("%.1f", this.armor_integrity) + "%");
        System.out.println("Laminar   : " + String.format("%.2f", this.laminar_flow_coefficient));
        System.out.println("Munitions : " + this.payload_count + " units ready");
        System.out.println("--------------------------------------------------");
    }
}
