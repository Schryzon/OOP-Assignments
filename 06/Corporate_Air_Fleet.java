import java.util.ArrayList;

/**
 * Corporate_Air_Fleet - Tactical combat fleet aggregation entity.
 * Demonstrates fixed static arrays (Combat_Aircraft[] ready_hangar) paired
 * with dynamic collections (ArrayList<Combat_Aircraft> reserve_reinforcements).
 */
public class Corporate_Air_Fleet {

    private String fleet_codename;
    private Corporate_Faction faction;
    private Combat_Aircraft[] ready_hangar;
    private ArrayList<Combat_Aircraft> reserve_reinforcements;
    private int active_aircraft_count;

    private static final int DEFAULT_HANGAR_CAPACITY = 8;

    public Corporate_Air_Fleet(String fleet_codename, Corporate_Faction faction, int hangar_capacity){
        this.fleet_codename = fleet_codename;
        this.faction = faction;
        int capacity = Math.max(hangar_capacity, DEFAULT_HANGAR_CAPACITY);
        this.ready_hangar = new Combat_Aircraft[capacity];
        this.reserve_reinforcements = new ArrayList<Combat_Aircraft>();
        this.active_aircraft_count = 0;
    }

    public boolean station_aircraft(Combat_Aircraft craft){
        if(craft == null){
            return false;
        }

        if(this.active_aircraft_count >= this.ready_hangar.length){
            System.out.println("[" + this.fleet_codename + "] Ready hangar full! Diverting " + craft.get_model_name() + " to reserve reinforcements.");
            reinforce_reserve(craft);
            return false;
        }

        this.ready_hangar[this.active_aircraft_count] = craft;
        this.active_aircraft_count++;
        System.out.println("[" + this.fleet_codename + "] Stationed " + craft.get_model_name() + " in hangar slot [" + (this.active_aircraft_count - 1) + "].");
        return true;
    }

    public void reinforce_reserve(Combat_Aircraft craft){
        if(craft != null){
            this.reserve_reinforcements.add(craft);
            System.out.println("[" + this.fleet_codename + "] Added " + craft.get_model_name() + " to reserve depot. (Reserve count: " + this.reserve_reinforcements.size() + ")");
        }
    }

    public void scramble_fleet_sorties(){
        System.out.println("==================================================");
        System.out.println("SCRAMBLING SORTIE: " + this.fleet_codename + " // " + this.faction);
        System.out.println("==================================================");

        for(int i = 0; i < this.active_aircraft_count; i++){
            Combat_Aircraft craft = this.ready_hangar[i];
            System.out.println("\n-- Launching Hangar Slot [" + i + "]: " + craft.get_model_name() + " --");
            craft.set_thrust(90.0);
            craft.deploy_countermeasures();
            craft.render_tactical_hud();
        }
    }

    public double calculate_fleet_firepower(){
        double total_firepower = 0.0;

        for(int i = 0; i < this.active_aircraft_count; i++){
            total_firepower += this.ready_hangar[i].get_payload_count() * 10.0;
        }

        for(Combat_Aircraft reserve_craft : this.reserve_reinforcements){
            total_firepower += reserve_craft.get_payload_count() * 10.0;
        }

        return total_firepower;
    }

    public int get_active_aircraft_count(){
        return this.active_aircraft_count;
    }

    public Combat_Aircraft get_aircraft(int index){
        if(index < 0 || index >= this.active_aircraft_count){
            return null;
        }
        return this.ready_hangar[index];
    }

    public String get_fleet_codename(){
        return this.fleet_codename;
    }

    public Corporate_Faction get_faction(){
        return this.faction;
    }
}
