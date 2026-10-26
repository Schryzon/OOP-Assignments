/**
 * Combat_Aircraft - Abstract base class for all Electrosphere tactical aircraft.
 * Implements Flight_Control_Contract and Weapon_Platform.
 * Encapsulates core flight invariants and enforces abstract tactical behaviors.
 */
public abstract class Combat_Aircraft implements Flight_Control_Contract, Weapon_Platform {

    protected String model_name;
    protected Corporate_Faction faction;
    protected double armor_integrity;
    protected double current_speed;
    protected double max_speed;
    protected int payload_count;

    private static final double MAX_STRUCTURAL_G = 12.0;

    public Combat_Aircraft(String model_name, Corporate_Faction faction, double max_speed, int payload_count){
        this.model_name = model_name;
        this.faction = faction;
        this.armor_integrity = 100.0;
        this.current_speed = 0.0;
        this.max_speed = Math.max(max_speed, 100.0);
        this.payload_count = Math.max(payload_count, 0);
    }

    // -- Flight Control Implementation --

    @Override
    public void set_thrust(double throttle_percent){
        if(throttle_percent < 0.0){
            this.current_speed = 0.0;
            return;
        }

        if(throttle_percent > 100.0){
            this.current_speed = this.max_speed;
            return;
        }

        this.current_speed = (throttle_percent / 100.0) * this.max_speed;
    }

    @Override
    public boolean execute_maneuver(double g_force){
        if(g_force <= 0.0){
            return false;
        }

        if(g_force > MAX_STRUCTURAL_G){
            System.out.println("[" + this.model_name + "] WARNING: High-G maneuver exceeded airframe threshold (" + g_force + "G)!");
            take_damage(5.0);
            return false;
        }

        return true;
    }

    @Override
    public double get_current_speed(){
        return this.current_speed;
    }

    // -- Weapon Platform Overloaded Implementation --

    @Override
    public boolean fire_weapon(){
        if(this.payload_count <= 0){
            System.out.println("[" + this.model_name + "] Munitions depleted! Cannot fire.");
            return false;
        }

        this.payload_count--;
        System.out.println("[" + this.model_name + "] Discharged standard pulse burst. Remaining payload: " + this.payload_count);
        return true;
    }

    @Override
    public boolean fire_weapon(int salvo_count){
        if(salvo_count <= 0){
            return false;
        }

        if(this.payload_count < salvo_count){
            System.out.println("[" + this.model_name + "] Insufficient payload for " + salvo_count + "-missile salvo. Available: " + this.payload_count);
            return false;
        }

        this.payload_count -= salvo_count;
        System.out.println("[" + this.model_name + "] Discharged " + salvo_count + "-missile salvo! Remaining payload: " + this.payload_count);
        return true;
    }

    @Override
    public boolean fire_weapon(String target_id, double lock_frequency){
        if(this.payload_count <= 0){
            System.out.println("[" + this.model_name + "] Cannot acquire lock on " + target_id + ": out of munitions.");
            return false;
        }

        if(lock_frequency < 5.0 || lock_frequency > 30.0){
            System.out.println("[" + this.model_name + "] Radar tracking failed: invalid frequency band " + lock_frequency + " GHz.");
            return false;
        }

        this.payload_count--;
        System.out.println("[" + this.model_name + "] Radar missile locked on target " + target_id + " at " + lock_frequency + " GHz! Fired.");
        return true;
    }

    @Override
    public void reload_munitions(int count){
        if(count > 0){
            this.payload_count += count;
        }
    }

    @Override
    public int get_payload_count(){
        return this.payload_count;
    }

    // -- State and Damage Encapsulation --

    public void take_damage(double raw_damage){
        if(raw_damage <= 0.0){
            return;
        }

        this.armor_integrity = Math.max(0.0, this.armor_integrity - raw_damage);
        if(this.armor_integrity == 0.0){
            System.out.println("[" + this.model_name + "] CRITICAL: Aircraft destroyed or shot down!");
        }
    }

    public String get_model_name(){
        return this.model_name;
    }

    public Corporate_Faction get_faction(){
        return this.faction;
    }

    public double get_armor_integrity(){
        return this.armor_integrity;
    }

    public double get_max_speed(){
        return this.max_speed;
    }

    // -- Abstract Contract Hooks --

    public abstract double calculate_radar_cross_section();

    public abstract void deploy_countermeasures();

    public abstract void render_tactical_hud();
}
