/**
 * Night_Raven - The X-49 Night Raven experimental superweapon.
 * Developed under General Resource's 'Darkness of Enigma' project and seized by Ouroboros.
 * Features an Opto-Neuron direct brainstem link and Sublimation consciousness transfer capability.
 */
public class Night_Raven extends Combat_Aircraft implements Sublimation_Capable {

    private double laser_cannon_charge_mw;
    private String sublimated_entity_id;
    private double neural_sync_ratio;

    public Night_Raven(double max_speed, int payload_count){
        super("X-49 Night Raven", Corporate_Faction.OUROBOROS, max_speed, payload_count);
        this.laser_cannon_charge_mw = 100.0;
        this.sublimated_entity_id = null;
        this.neural_sync_ratio = 0.0;
    }

    public boolean fire_laser_cannon(double charge_demand){
        if(charge_demand <= 0.0){
            return false;
        }

        if(this.laser_cannon_charge_mw < charge_demand){
            System.out.println("[" + this.model_name + "] Laser capacitor depleted (" + String.format("%.1f", this.laser_cannon_charge_mw) + " MW). Recharge needed.");
            return false;
        }

        this.laser_cannon_charge_mw -= charge_demand;
        System.out.println("[" + this.model_name + "] DISCHARGED HIGH-OUTPUT LASER CANNON (" + charge_demand + " MW)! Capacitor: " + String.format("%.1f", this.laser_cannon_charge_mw) + " MW");
        return true;
    }

    @Override
    public boolean upload_consciousness(String pilot_neural_id){
        if(pilot_neural_id == null || pilot_neural_id.isEmpty()){
            return false;
        }

        this.sublimated_entity_id = pilot_neural_id;
        this.neural_sync_ratio = 0.998;
        System.out.println("[" + this.model_name + "] Consciousness sublimation verified: Entity [" + pilot_neural_id + "] bound to Opto-Neuron core.");
        return true;
    }

    @Override
    public double get_neural_sync_ratio(){
        return this.neural_sync_ratio;
    }

    @Override
    public boolean is_sublimated(){
        return this.sublimated_entity_id != null;
    }

    @Override
    public double calculate_radar_cross_section(){
        // Flying-wing stealth geometry with active Aeon emission
        return 0.015;
    }

    @Override
    public void deploy_countermeasures(){
        System.out.println("[" + this.model_name + "] Activating Opto-Neuron full-spectrum radar/optical scrambling field.");
    }

    @Override
    public void render_tactical_hud(){
        System.out.println("--------------------------------------------------");
        System.out.println("OUROBOROS CYBERSPACE COCKPIT // " + this.model_name);
        System.out.println("Faction     : " + this.faction);
        System.out.println("Airspeed    : " + String.format("%.1f", this.current_speed) + " / " + this.max_speed + " km/h");
        System.out.println("Armor       : " + String.format("%.1f", this.armor_integrity) + "%");
        System.out.println("Laser Cap   : " + String.format("%.1f", this.laser_cannon_charge_mw) + " MW");
        System.out.println("Sublimation : " + (is_sublimated() ? "ACTIVE (" + this.sublimated_entity_id + " // " + String.format("%.1f%%", this.neural_sync_ratio * 100) + ")" : "OFFLINE"));
        System.out.println("--------------------------------------------------");
    }
}
