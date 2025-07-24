package relics;

import java.util.function.Supplier;

import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;
import relics.relicsAbilities.HermesSandalsAbility;
import relics.relicsAbilities.RelicOfInfernalBeastAbility;
import relics.relicsAbilities.RelicOfOblivionAbility;
import relics.relicsAbilities.RelicOfTheMinersFortuneAbility;
import relics.relicsAbilities.RelicOfTheWanderingStarAbility;
import relics.relicsAbilities.SonicBowAbility;
import relics.relicsItems.AshenKingItem;
import relics.relicsItems.HermesSandalsItem;
import relics.relicsItems.HollowBonesItem;
import relics.relicsItems.PhantomBladeItem;
import relics.relicsItems.RelicOfOblivionItem;
import relics.relicsItems.RelicOfTheAlchemistItem;
import relics.relicsItems.RelicOfTheEndseerItem;
import relics.relicsItems.RelicOfTheForsakenItem;
import relics.relicsItems.RelicOfTheInfernalBeastItem;
import relics.relicsItems.RelicOfTheMinersFortuneItem;
import relics.relicsItems.RelicOfThePathFinderItem;
import relics.relicsItems.RelicOfTheSharpenedFangItem;
import relics.relicsItems.RelicOfTheTitansWillItem;
import relics.relicsItems.RelicOfTheWanderingStarItem;
import relics.relicsItems.SonicBowItem;

public enum Relic {
	ASHEN_KING(new AshenKingItem().ashen_king_item, null, "§c§lASHEN KING"),
	HERMES_SANDALS(new HermesSandalsItem().hermes_sandals_item, HermesSandalsAbility::new, "§c§lHERMES SANDALS"),
	PHANTOM_BLADE(new PhantomBladeItem().phantom_blade_item, null, "§c§lPHANTOM BLADE"),
	HOLLOW_BONES(new HollowBonesItem().hollow_bones_item, null, "§c§lHOLLOW BONES"),
	RELIC_OF_OBLIVION(new RelicOfOblivionItem().relic_of_oblivion_item, RelicOfOblivionAbility::new, "§c§lRELIC OF OBLIVION"),
	RELIC_OF_SHARPENED_FANG(new RelicOfTheSharpenedFangItem().sharpened_fang_item, null, "§c§lRELIC OF THE SHARPENED FANG"),
	RELIC_OF_THE_PATHFINDER(new RelicOfThePathFinderItem().path_finder_relic_item, null, "§c§lRELIC OF THE PATH FINDER"),
	RELIC_OF_THE_WANDERING_STAR(new RelicOfTheWanderingStarItem().wandering_star_relic_item, RelicOfTheWanderingStarAbility::new, "§c§lRELIC OF THE WANDERING STAR"),
	RELIC_OF_THE_MINERS_FORTUNE(new RelicOfTheMinersFortuneItem().miners_fortune_item, RelicOfTheMinersFortuneAbility::new, "§c§lRELIC OF THE MINERS FORTUNE"),
	RELIC_OF_THE_ALCHEMIST(new RelicOfTheAlchemistItem().alchemist_relic_item, null, "§c§lRELIC OF THE ALCHEMIST"),
	RELIC_OF_THE_FORSAKEN(new RelicOfTheForsakenItem().forsaken_relic_item, null, "§c§lRELIC OF THE FORSAKEN"),
	RELIC_OF_THE_TITANS_WILL(new RelicOfTheTitansWillItem().titans_will_relic_item, null, "§c§lRELIC OF THE TITANS WILL"),
	RELIC_OF_THE_ENDSEER(new RelicOfTheEndseerItem().endseer_relic_item, null, "§c§lRELIC OF THE ENDSEER"),
	RELIC_OF_INFERNAL_BEAST(new RelicOfTheInfernalBeastItem().infernal_beast_item, RelicOfInfernalBeastAbility::new, "§c§lRELIC OF THE INFERNAL BEAST"),
	SONIC_BOW(new SonicBowItem().sonic_bow_item, SonicBowAbility::new, "§c§lSONIC BOW RELIC");
	
	private ItemStack item;
	String name;
	private Supplier<RelicAbility> ability;
	
	Relic(ItemStack item, Supplier<RelicAbility> ability, String name) {
		this.item = item;
		this.ability = ability;
		this.name = name;
	}
	
	public RelicAbility getAbility() {
		if (!UsefullFunctions.checkIfCanUse()) {
			return null;
		}
		if (ability == null) {
			return null;
		}
		return this.ability.get();
	}
	
	public ItemStack getRelicItem() {
		if (!UsefullFunctions.checkIfCanUse()) {
			return null;
		}
		return this.item;
	}
	
	public String getName() {
		if (!UsefullFunctions.checkIfCanUse()) {
			return null;
		}
		return this.name;
	}
	
	
}
