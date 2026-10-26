/**
 * Weapon_Platform - Tactical offensive armament interface.
 * Defines single, salvo, and radar-guided weapon delivery contracts.
 */
public interface Weapon_Platform {

    boolean fire_weapon();

    boolean fire_weapon(int salvo_count);

    boolean fire_weapon(String target_id, double lock_frequency);

    void reload_munitions(int count);

    int get_payload_count();
}
