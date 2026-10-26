/**
 * Su43_Berkut - General Resource heavy forward-swept wing multi-role fighter.
 * Equipped with COFFIN generation 2 opto-neuron arrays and thrust-vectoring canards.
 */
public class Su43_Berkut extends Combat_Aircraft {

    private boolean canards_vectoring;

    public Su43_Berkut(Corporate_Faction faction, double max_speed, int payload_count){
        super("Su-43 Berkut", faction, max_speed, payload_count);
        this.canards_vectoring = true;
    }

    public void set_canards_vectoring(boolean active){
        this.canards_vectoring = active;
    }

    @Override
    public double calculate_radar_cross_section(){
        // Forward-swept wings with reinforced titanium armor yield larger signature
        return 0.12;
    }

    @Override
    public void deploy_countermeasures(){
        System.out.println("[" + this.model_name + "] Dispensing high-temperature magnesium flares and dual-band chaff corridors.");
    }

    @Override
    public void render_tactical_hud(){
        System.out.println("--------------------------------------------------");
        System.out.println("GENERAL RESOURCE COFFIN-2 HUD // " + this.model_name);
        System.out.println("Faction     : " + this.faction);
        System.out.println("Airspeed    : " + String.format("%.1f", this.current_speed) + " / " + this.max_speed + " km/h");
        System.out.println("Armor       : " + String.format("%.1f", this.armor_integrity) + "%");
        System.out.println("Canards     : " + (this.canards_vectoring ? "ACTIVE VECTORING" : "LOCKED"));
        System.out.println("Munitions   : " + this.payload_count + " units ready");
        System.out.println("--------------------------------------------------");
    }
}
