/**
 * Coffin_Operator - Neural pilot entity connected via ENSI interface.
 * Models association (Has-A relationship) with Combat_Aircraft.
 */
public class Coffin_Operator {

    private String callsign;
    private Corporate_Faction faction;
    private double neuro_compatibility_index;
    private Combat_Aircraft assigned_aircraft;

    public Coffin_Operator(String callsign, Corporate_Faction faction, double neuro_compatibility_index){
        this.callsign = callsign;
        this.faction = faction;
        this.neuro_compatibility_index = Math.max(0.0, Math.min(1.0, neuro_compatibility_index));
        this.assigned_aircraft = null;
    }

    public void assign_aircraft(Combat_Aircraft craft){
        this.assigned_aircraft = craft;
        if(craft != null){
            System.out.println("[COFFIN] Operator " + this.callsign + " assigned to airframe " + craft.get_model_name());
        }
    }

    public Combat_Aircraft get_assigned_aircraft(){
        return this.assigned_aircraft;
    }

    public boolean sync_neural_link(){
        if(this.assigned_aircraft == null){
            System.out.println("[COFFIN] Neural sync failed: No aircraft assigned to operator " + this.callsign);
            return false;
        }

        if(this.neuro_compatibility_index < 0.4){
            System.out.println("[COFFIN] Synapse rejection: Neuro-compatibility index too low (" + this.neuro_compatibility_index + ")");
            return false;
        }

        System.out.println("[COFFIN] ENSI neural synchronization established for " + this.callsign + " (Sync: " + String.format("%.1f%%", this.neuro_compatibility_index * 100) + ") with " + this.assigned_aircraft.get_model_name());
        return true;
    }

    public String get_callsign(){
        return this.callsign;
    }

    public Corporate_Faction get_faction(){
        return this.faction;
    }

    public double get_neuro_compatibility_index(){
        return this.neuro_compatibility_index;
    }
}
