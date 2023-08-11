package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Language;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Random;

// TODO: move the languages into the enum or its own class
public class Announcer extends Module {

    protected final EnumProperty<Language> language =
            new EnumProperty<>(
                    Language.ENGLISH,
                    new String[]{"Language", "Mode", "Type"},
                    "The language we will announce stuff in."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(15, 1, 60,
                    new String[]{"Delay", "delayington"},
                    "Delay between sending different messages."
            );

    protected final Property<Boolean> move =
            new Property<>(
                    true,
                    new String[]{"Move", "movement", "walk", "distance"},
                    "Announces the distance we have walked."
            );

    protected final Property<Boolean> jump =
            new Property<>(
                    true,
                    new String[]{"Jump", "jumped"},
                    "Announces when we have jumped."
            );
    protected final Property<Boolean> mine =
            new Property<>(
                    true,
                    new String[]{"Break", "Destroyed", "broken", "destroylonely"},
                    "Announces the block that we destroyed."
            );

    protected final Property<Boolean> place =
            new Property<>(
                    true,
                    new String[]{"Place", "placements"},
                    "Announces the block that we placed."
            );

    protected final Property<Boolean> eat =
            new Property<>(
                    true,
                    new String[]{"Eat", "Eaten"},
                    "Announces the food that we have eaten."
            );

    protected final Property<Boolean> greenText =
            new Property<>(
                    true,
                    new String[]{"GreenText", ">", "Autogreentext"},
                    "Puts > in front of you messages to make it green on some servers."
            );

    protected final Property<Boolean> cycle =
            new Property<>(
                    true,
                    new String[]{"Cycle", "langaugecycle", "languageswitch"},
                    "Cycles through all the languages."
            );

    protected String walkMessage;
    protected String placeMessage;
    protected String jumpMessage;
    protected String breakMessage;
    protected String eatMessage;

    protected StopWatch timer = new StopWatch();
    protected StopWatch moveTimer = new StopWatch();
    protected StopWatch jumpTimer = new StopWatch();
    protected Random random = new Random();

    protected Block brokenBlock;
    protected ItemStack placeStack;
    protected ItemStack foodStack;
    protected double speed;

    protected LinkedHashMap<Type, Float> events = new LinkedHashMap<>();

    public Announcer() {
        super("Announcer", new String[]{"Announcer", "anounce", "greeter", "greet"}, "How to get muted fast.", Category.MISC);
        this.offerProperties(language, delay, move, jump, mine, place, eat, greenText, cycle);
        this.offerListeners(new ListenerBreak(this), new ListenerPlace(this), new ListenerMotion(this), new ListenerJump(this),
                new ListenerEat(this), new ListenerDeath(this), new ListenerLogout(this), new ListenerUpdate(this));
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        reset();
    }

    @Override
    public void onWorldLoad() {
        reset();
    }

    @SuppressWarnings({"SpellCheckingInspection"})
    protected void setMessages() {
        switch (language.getValue()) {
            case ENGLISH: {
                walkMessage     = "I just moved {blocks} blocks thanks to SexMaster.CC!";
                placeMessage    = "I just placed {amount} {name} thanks to SexMaster.CC!";
                jumpMessage     = "I just jumped thanks to SexMaster.CC!";
                breakMessage    = "I just broke {amount} {name} thanks to SexMaster.CC!";
                eatMessage      = "I just ate {amount} {name} thanks to SexMaster.CC!";
                break;
            }
            case SPANISH: {
                walkMessage     = "Acabo de moverme {blocks} bloques gracias a SexMaster.CC!";
                placeMessage    = "Acabo de colocar {amount} {name} gracias a SexMaster.CC!";
                jumpMessage     = "Acabo de saltar gracias a SexMaster.CC!";
                breakMessage    = "Acabo de romper {amount} {name} gracias a SexMaster.CC!";
                eatMessage      = "Acabo de comer {amount} {name} gracias a SexMaster.CC!";
                break;
            }
            case RUSSIAN: { //most of this languages are translated weirdly
                walkMessage     = "\u042F \u0442\u043E\u043B\u044C\u043A\u043E \u0447\u0442\u043E \u043F\u0435\u0440\u0435\u0435\u0445\u0430\u043B {blocks} \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F SexMaster.CC! (\u25e3_\u25e2)";
                placeMessage    = "\u042F \u0442\u043E\u043B\u044C\u043A\u043E \u0447\u0442\u043E \u0440\u0430\u0437\u043C\u0435\u0441\u0442\u0438\u043B {amount} {name} \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F SexMaster.CC! (\u25e3_\u25e2)";
                jumpMessage     = "\u042F \u043F\u0440\u043E\u0441\u0442\u043E \u043F\u0440\u044B\u0433\u043D\u0443\u043B \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F SexMaster.CC! (\u25e3_\u25e2)";
                breakMessage    = "\u042F \u0442\u043E\u043B\u044C\u043A\u043E \u0447\u0442\u043E \u0440\u0430\u0437\u0431\u0438\u043B {amount} {name} \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F SexMaster.CC! (\u25e3_\u25e2) ";
                eatMessage      = "\u044F \u0442\u043E\u043B\u044C\u043A\u043E \u0447\u0442\u043E \u0441\u044A\u0435\u043B {amount} {name} \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F SexMaster.CC! (\u25e3_\u25e2)";
                break;
            }
            case GERMAN: {
                walkMessage     = "Ich habe gerade {blocks} Blöcke verschoben, dank SexMaster.CC!";
                placeMessage    = "Ich habe gerade {amount} {name} dank SexMaster.CC!";
                jumpMessage     = "Ich bin gerade dank SexMaster.CC!";
                breakMessage    = "Ich habe gerade {amount} {name} dank SexMaster.CC!";
                eatMessage      = "Ich habe gerade {amount} {name} dank SexMaster.CC!";
                break;
            }
            case ARABIC: {
                walkMessage     = "\u0644\u0642\u062F \u0642\u0645\u062A \u0644\u0644\u062A\u0648 \u0628\u0646\u0642\u0644 {blocks} \u0645\u0646 \u0627\u0644\u0643\u062A\u0644 \u0628\u0641\u0636\u0644 SexMaster.CC!";
                placeMessage    = "\u0644\u0642\u062F \u0642\u062F\u0645\u062A \u0644\u0644\u062A\u0648 {amount} {name} \u0628\u0641\u0636\u0644 SexMaster.CC!";
                jumpMessage     = "\u0644\u0642\u062F \u0642\u0641\u0632\u062A \u0644\u0644\u062A\u0648 \u0628\u0641\u0636\u0644 SexMaster.CC!";
                breakMessage    = "\u0644\u0642\u062F \u0643\u0633\u0631\u062A {amount} {name} \u0628\u0641\u0636\u0644 SexMaster.CC!";
                eatMessage      = "\u0644\u0642\u062F \u0623\u0643\u0644\u062A \u0644\u0644\u062A\u0648 {amount} {name} \u0628\u0641\u0636\u0644 SexMaster.CC!";
                break;
            }
            case FRENCH: {
                walkMessage     = "Je viens de bouger {blocks} pieds grâce à SexMaster.CC!";
                placeMessage    = "Je viens de placer {amount} {name} grâce à SexMaster.CC!";
                jumpMessage     = "Je viens de sauter grâce à SexMaster.CC!";
                breakMessage    = "Je viens de casser {amount} {name} grâce à SexMaster.CC!";
                eatMessage      = "Je viens de manger {amount} {name} grâce à SexMaster.CC!";
                break;
            }
            case AMHARIC: {
                walkMessage     = "\u12A0\u1201\u1295 \u1260SexMaster.CC \u121D\u12AD\u1295\u12EB\u1275 {blocks} \u1270\u122B\u1218\u12F5\u12A9";
                placeMessage    = "\u1208 SexMaster.CC \u121D\u1235\u130B\u1293\u12EC\u1295 \u12A0\u1201\u1295 \u12A0\u1235\u1240\u121D\u132B\u1208\u1201 {amount} {name}!";
                jumpMessage     = "\u12A5\u1294 \u1265\u127B SexMaster.CC \u121D\u1235\u130B\u1293 \u12D8\u120E!";
                breakMessage    = "\u12A0\u1201\u1295 \u1208 SexMaster.CC \u121D\u1235\u130B\u1293\u12EC\u1295 \u12A0\u1245\u122D\u1264\u12EB\u1208\u1201 {amount} {name}!";
                eatMessage      = "\u1208 SexMaster.CC \u121D\u1235\u130B\u1293 \u12ED\u130D\u1263\u12CD {amount} {name} \u1260\u120D\u127B\u1208\u1201!";
                break;
            }
            case SLOVAK: {
                walkMessage     = "Pr\u00E1ve som presunul {blocks} blokov v\u010Faka SexMaster.CC!";
                placeMessage    = "Pr\u00E1ve som umiestnil {amount} {name} v\u010Faka SexMaster.CC!";
                jumpMessage     = "Pr\u00E1ve som sko\u010Dil v\u010Faka SexMaster.CC!";
                breakMessage    = "Pr\u00E1ve som zlomil {amount} {name} v\u010Faka SexMaster.CC!";
                eatMessage      = "Pr\u00E1ve som zjedol {amount} {name} v\u010Faka SexMaster.CC!";
                break;
            }
            case SLOVENIAN: { // thx to cpv for transl;ations
                walkMessage     = "Pravkar sem se premaknil {blocks} kock zahvaljujo\u010De SexMaster.CC!";
                placeMessage    = "Pravkar sem postavil {amount} {name} zahvaljujo\u010De SexMaster.CC!";
                jumpMessage     = "Pravkar sem sko\u010Dil zahvaljujo\u010De SexMaster.CC!";
                breakMessage    = "Pravkar sem zlomil {amount} {name} zahvaljujo\u010De SexMaster.CC!";
                eatMessage      = "Pravkar sem pojedel {amount} {name} zahvaljujo\u010De SexMaster.CC!";
                break;
            }
            case SWEDISH: {
                walkMessage     = "Jag har just flyttat {blocks} fot tack vare SexMaster.CC";
                placeMessage    = "Jag har just placerat {amount} {name} tack vare SexMaster.CC";
                jumpMessage     = "Jag hoppade just tack vare SexMaster.CC";
                breakMessage    = "Jag förstörde just {amount} {name} tack vare SexMaster.CC";
                eatMessage      = "Jag åt just {amount} {name} tack vare SexMaster.CC";
                break;
            }
            case INDONESIAN: {
                walkMessage     = "Saya baru saja berjalan kaki {blocks} berkat SexMaster.CC!";
                placeMessage    = "Saya baru saja menempatkan {amount} {name} berkat SexMaster.CC!";
                jumpMessage     = "Saya baru saja melompat berkat SexMaster.CC!";
                breakMessage    = "Saya baru saja menghancurkan {amount} {name} berkat SexMaster.CC!";
                eatMessage      = "Saya baru saja makan {amount} {name} berkat SexMaster.CC!";
                break;
            }
            case FINNISH: {
                walkMessage     = "Kävelin juuri {blocks} jalat ansiosta SexMaster.CC.";
                placeMessage    = "Sijoitin juuri {amount} {name} kiitos SexMaster.CC.";
                jumpMessage     = "Hyppäsin juuri, kiitos SexMaster.CC";
                breakMessage    = "Tuhosin juuri {amount} {name} kiitos SexMaster.CC.";
                eatMessage      = "Söin juuri {amount} {name} kiitos SexMaster.CC.";
                break;
            }
            case ESTONIAN: {
                walkMessage     = "Ma lihtsalt kõndisin {blocks} jalad tänu SexMaster.CC!";
                placeMessage    = "Ma just paigutasin {amount} {name} tänu SexMaster.CC!";
                jumpMessage     = "Ma lihtsalt hüppasin, tänu SexMaster.CC!";
                breakMessage    = "Ma just murdsin {amount} {name} tänu SexMaster.CC!";
                eatMessage      = "Ma just sõin {amount} {name} tänu SexMaster.CC!";
                break;
            }
            case NORWEGIAN: {
                walkMessage     = "Jeg gikk nettopp {blocks} føtter takket være SexMaster.CC";
                placeMessage    = "Jeg har nettopp plassert {amount} {name} Takk til SexMaster.CC";
                jumpMessage     = "Jeg bare hoppet takket være SexMaster.CC";
                breakMessage    = "Jeg ødela nettopp {amount} {name} Takket være SexMaster.CC";
                eatMessage      = "Jeg spiste nettopp {amount} {name} Takket være SexMaster.CC";
                break;
            }
            case ROMAN: {
                walkMessage     = "Tocmai am mers {blocks} picioare datorită SexMaster.CC";
                placeMessage    = "Tocmai am plasat {amount} {name} Datorită SexMaster.CC";
                jumpMessage     = "Tocmai am sărit, mulțumită SexMaster.CC";
                breakMessage    = "Tocmai am distrus {amount} {name} Mulțumită lui SexMaster.CC";
                eatMessage      = "Tocmai am mâncat {amount} {name} Mulțumită lui SexMaster.CC";
                break;
            }
            case TURK: {
                walkMessage     = "SexMaster.CC sayesinde {blocks} metre yürüdüm";
                placeMessage    = "Az önce SexMaster.CC sayesinde {amount} {name} yerleştirdim.";
                jumpMessage     = "SexMaster.CC sayesinde atladım.";
                breakMessage    = "Az önce sexMaster.CC sayesinde {amount} {name}'i yok ettim";
                eatMessage      = "Az önce SexMaster.CC sayesinde {amount} {name} yedim.";
                break;
            }
            case POLISH: {
                walkMessage     = "Właśnie przeszedłem {blocks} metrów dzięki SexMaster.CC";
                placeMessage    = "Właśnie umieściłem {amount} {name} Dzięki SexMaster.CC";
                jumpMessage     = "Właśnie skoczyłem, dzięki SexMaster.CC";
                breakMessage    = "Właśnie zniszczyłem {amount} {name} Dzięki SexMaster.CC";
                eatMessage      = "Zjadłem właśnie {amount} {name} Dzięki SexMaster.CC";
                break;
            }
            case HUNGARIAN: {
                walkMessage     = "Én csak sétáltam {blocks} lábak köszönhetően SexMaster.CC";
                placeMessage    = "Most helyeztem el {amount} {name} Köszönöm a SexMaster.CC.";
                jumpMessage     = "Én csak ugrottam, Köszönöm, hogy sexMaster.CC";
                breakMessage    = "Épp most törtem {amount} {name} Köszönöm SexMaster.CC";
                eatMessage      = "Épp most ettem {amount} {name} Köszönöm SexMaster.CC";
                break;
            }
            case JAPANESE: {
                walkMessage     = "SexMaster.CC \u306E\u304A\u304B\u3052\u3067\u3001{blocks} \u306E\u8DB3\u3092\u52D5\u304B\u3057\u305F\u3060\u3051\u3067\u3059\u3002 \uFF3C\uFF08\uFF3E\u25CB\uFF3E\uFF09\u4EBA\uFF08\uFF3E\u25CB\uFF3E\uFF09\uFF0F";
                placeMessage    = "SexMaster.CC \u306E\u304A\u304B\u3052\u3067 {amount} {name} \u3092\u914D\u7F6E\u3057\u305F\u3068\u3053\u308D\u3067\u3059\u3002 \u30FD(\uFF9F\uFF70\uFF9F*\u30FD)\u30FD(*\uFF9F\uFF70\uFF9F*)\uFF89(\uFF89*\uFF9F\uFF70\uFF9F)\uFF89";
                jumpMessage     = "SexMaster.CC \u306E\u304A\u304B\u3052\u3067\u30B8\u30E3\u30F3\u30D7\u3067\u304D\u307E\u3057\u305F\u3002 \u30FD(´\u25BD\uFF40)\u30CE";
                breakMessage    = "SexMaster.CC \u306E\u304A\u304B\u3052\u3067 {amount} {name} \u3092\u7834\u58CA\u3057\u305F\u3068\u3053\u308D\u3067\u3059\u3002 (\uFF61\u273F\u203F\u273F\uFF61)";
                eatMessage      = "SexMaster.CC \u306E\u304A\u304B\u3052\u3067 {amount} {name} \u3092\u98DF\u3079\u305F\u3068\u3053\u308D\u3067\u3059\u3002 \uFF08\u30DF\uFFE3\u30FC\uFFE3\u30DF\uFF09";
                break;
            }
            case CHINESE: {
                walkMessage     = "\u6211\u521A\u642C\u4E86 {blocks} \u8C22\u8C22 SexMaster.CC\uFF01 (\u3065\uFF61\u25D5\u203F\u203F\u25D5\uFF61)\u3065";
                placeMessage    = "\u6211\u521A\u521A\u653E\u7F6E\u4E86 {amount} {name} \u8C22\u8C22 SexMaster.CC\uFF01 (\u25CF\u03C9\uFF40\u25CF)";
                jumpMessage     = "\u6211\u521A\u521A\u8DF3\u4E86 \u8C22\u8C22 SexMaster.CC\uFF01 \u2267\u25E1\u2266";
                breakMessage    = "\u6211\u521A\u521A\u6253\u7834\u4E86 {amount} {name} \u8C22\u8C22 SexMaster.CC\uFF01 (\u273F\u25E0\u203F\u25E0)";
                eatMessage      = "\u6211\u521A\u5403\u4E86 {amount} {name} \u8C22\u8C22 SexMaster.CC\uFF01 (\uFF89\u25D5\u30EE\u25D5) \uFF89*:\uFF65\uFF9F\u2727";
                break;
            }
            case LOLCAT: {
                walkMessage     = "I JUS MOVD {blocks} BLOCKZ THX 2 SEKSMASTR.CC!";
                placeMessage    = "I JUS PLACD {amount} {name} THX 2 SEKSMASTR.CC!";
                jumpMessage     = "I JUS JUMPD THX 2 SEKSMASTR.CC!";
                breakMessage    = "I JUS BROKE {amount} {name} THX 2 SEKSMASTR.CC!";
                eatMessage      = "I JUS EATD {amount} {name} THX 2 SEKSMASTR.CC!";
                break;
            }
            case PRAYER: {
                walkMessage     = "Inshallah my brothers I just walked {blocks} feet thanks to SexMaster.CC";
                placeMessage    = "Alhamdulillah I just placed {amount} {name} thanks to SexMaster.CC";
                jumpMessage     = "Bismillah I just jumped thanks to SexMaster.CC";
                eatMessage      = "Mashallah I just ate {amount} {name} thanks to SexMaster.CC";
                breakMessage    = "Subhanallah I just destroyed {amount} {name} thanks to SexMaster.CC";
                break;
            }
            case PIRATE: {
                walkMessage     = "I jus' walked {blocks} feet thanks t' SexMaster.CC!";
                placeMessage    = "I jus' placed {amount} {name} thanks t' SexMaster.CC!";
                jumpMessage     = "I jus' jumped thanks t' SexMaster.CC"; // fucking shitty pirate language dont know any words
                eatMessage      = "I jus' ate {amount} {name} thanks t' SexMaster.CC";
                breakMessage    = "I jus' destroyed {amount} {name} thanks t' SexMaster.CC";
                break;
            }
            case NFT: {
                walkMessage     = "I just walked {blocks} feet follow me on rumble https://rumble.com/BitCrypto89";
                jumpMessage     = "I just jumped follow me on rumble https://rumble.com/BitCrypto89";
                placeMessage    = "I just placed {amount} {name} follow me on rumble https://rumble.com/BitCrypto89";
                eatMessage      = "I just ate {amount} {name} follow me on rumble https://rumble.com/BitCrypto89";
                breakMessage    = "I just destroyed {amount} {name} follow me on rumble https://rumble.com/BitCrypto89";
                break;
            }
            case HEBREW: {
                walkMessage     = "\u05D6\u05D4 \u05E2\u05EA\u05D4 \u05D4\u05E2\u05D1\u05E8\u05EA\u05D9 {blocks} \u05D1\u05DC\u05D5\u05E7\u05D9\u05DD \u05D4\u05D5\u05D3\u05D5\u05EA \u05DC- SexMaster.CC!";
                placeMessage    = "\u05D4\u05E8\u05D2\u05E2 \u05D4\u05E0\u05D7\u05EA\u05D9 {amount} {name} \u05D1\u05D6\u05DB\u05D5\u05EA SexMaster.CC!";
                jumpMessage     = "\u05E4\u05E9\u05D5\u05D8 \u05E7\u05E4\u05E6\u05EA\u05D9 \u05D1\u05D6\u05DB\u05D5\u05EA SexMaster.CC!";
                breakMessage    = "\u05E4\u05E9\u05D5\u05D8 \u05E9\u05D1\u05E8\u05EA\u05D9 \u05D0\u05EA {amount} {name} \u05D1\u05D6\u05DB\u05D5\u05EA SexMaster.CC!";
                eatMessage      = "\u05D4\u05E8\u05D2\u05E2 \u05D0\u05DB\u05DC\u05EA\u05D9 {amount} {name} \u05D1\u05D6\u05DB\u05D5\u05EA SexMaster.CC!";
                break;
            }
            case BANGLA: {
                walkMessage     = "\u0986\u09AE\u09BF \u098F\u0987\u09AE\u09BE\u09A4\u09CD\u09B0 SexMaster.CC \u0995\u09C7 \u09A7\u09A8\u09CD\u09AF\u09AC\u09BE\u09A6 {blocks} \u09AC\u09CD\u09B2\u0995\u0997\u09C1\u09B2\u09BF \u09B8\u09B0\u09BF\u09AF\u09BC\u09C7\u099B\u09BF!";
                placeMessage    = "\u0986\u09AE\u09BF \u098F\u0987\u09AE\u09BE\u09A4\u09CD\u09B0 SexMaster.CC \u0995\u09C7 \u09A7\u09A8\u09CD\u09AF\u09AC\u09BE\u09A6 {amount} {name} \u09B0\u09BE\u0996\u09B2\u09BE\u09AE!";
                jumpMessage     = "\u0986\u09AE\u09BF \u09B6\u09C1\u09A7\u09C1 SexMaster.CC \u09A7\u09A8\u09CD\u09AF\u09AC\u09BE\u09A6 \u09B2\u09BE\u09AB!";
                breakMessage    = "\u0986\u09AE\u09BF \u098F\u0987\u09AE\u09BE\u09A4\u09CD\u09B0 SexMaster.CC \u0995\u09C7 \u09A7\u09A8\u09CD\u09AF\u09AC\u09BE\u09A6 {amount} {name} \u09AD\u09C7\u0999\u09C7\u099B\u09BF!";
                eatMessage      = "\u0986\u09AE\u09BF \u098F\u0987\u09AE\u09BE\u09A4\u09CD\u09B0 SexMaster.CC \u0995\u09C7 \u09A7\u09A8\u09CD\u09AF\u09AC\u09BE\u09A6 {amount} {name} \u0996\u09C7\u09AF\u09BC\u09C7\u099B\u09BF!";
                break;
            }
            case KOREAN: {
                walkMessage     = "SexMaster.CC \uB355\uBD84\uC5D0 \uBC29\uAE08 {blocks} \uBE14\uB85D\uC744 \uC62E\uACBC\uC2B5\uB2C8\uB2E4!";
                placeMessage    = "SexMaster.CC \uB355\uBD84\uC5D0 \uBC29\uAE08 {amount} {name}\uC744(\uB97C) \uBC30\uCE58\uD588\uC2B5\uB2C8\uB2E4!";
                jumpMessage     = "SexMaster.CC \uB355\uBD84\uC5D0 \uB6F0\uC5B4\uB0B4\uB838\uC2B5\uB2C8\uB2E4!";
                breakMessage    = "SexMaster.CC \uB355\uBD84\uC5D0 \uBC29\uAE08 {amount} {name}\uC744(\uB97C) \uAE68\uB728\uB838\uC2B5\uB2C8\uB2E4!";
                eatMessage      = "\uBC29\uAE08 SexMaster.CC \uB355\uBD84\uC5D0 {amount} {name}\uC744(\uB97C) \uBA39\uC5C8\uC2B5\uB2C8\uB2E4!";
                break;
            }
            case ALBANIA: {
                walkMessage     = "Sapo zhvendosa {blloqe} blloqe fal\u00EB SexMaster.CC!";
                placeMessage    = "Sapo vendosa {amount} {name} fal\u00EB SexMaster.CC!";
                jumpMessage     = "Sapo u hodha fal\u00EB SexMaster.CC!";
                breakMessage    = "Sapo kam thyer {samount} {name} fal\u00EB SexMaster.CC!";
                eatMessage      = "Sapo h\u00EBngra {samount} {name} fal\u00EB SexMaster.CC";
                break;
            }
            case HAWAIIAN: {
                walkMessage     = "Ua ho\u02BBone\u02BBe au i n\u0101 poloka {blocks} mahalo i\u0101 SexMaster.CC!";
                placeMessage    = "Ua kau au i {ka nui} {name} mahalo i\u0101 SexMaster.CC!";
                jumpMessage     = "Ua lele wale au i ka mahalo i\u0101 SexMaster.CC!";
                breakMessage    = "Ua uhaki au i {ka nui} {name} mahalo i\u0101 SexMaster.CC!";
                eatMessage      = "Ua \u02BBai wale au i {ka nui} {name} mahalo i\u0101 SexMaster.CC!";
                break;
            }
            case ITALY: {
                walkMessage     = "Ho appena spostato {blocks} blocchi grazie a SexMaster.CC!";
                placeMessage    = "Ho appena piazzato {amount} {name} grazie a SexMaster.CC!";
                jumpMessage     = "Sono appena saltato grazie a SexMaster.CC!";
                breakMessage    = "Ho appena rotto {amount} {name} grazie a SexMaster.CC!";
                eatMessage      = "Ho appena mangiato {amount} {name} grazie a SexMaster.CC!";
                break;
            }
            case LATIN: {
                walkMessage     = "Modo gratias ago SexMaster.CC ad caudices {blocks} commotus sum!";
                placeMessage    = "Modo posui {amount} {name} gratias SexMaster.CC!";
                jumpMessage     = "Modo gratias SexMaster.CC . laetabundus";
                breakMessage    = "Modo fregi {amount} {name} gratias SexMaster.CC!";
                eatMessage      = "Modo comedi {amount} {name} gratias SexMaster.CC!";
                break;
            }
            case GANDA: {
                walkMessage     = "Naakasenguka {blocks} blocks olw'okuba SexMaster.CC!";
                placeMessage    = "Naakateeka {amount} {name} olw'okusiima SexMaster.CC!";
                jumpMessage     = "Nze naakabuuka nga nneebaza SexMaster.CC!";
                breakMessage    = "Naakamenya {amount} {name} olw'okusiima SexMaster.CC!";
                eatMessage      = "Naakalya {amount} {name} olw'okusiima SexMaster.CC!";
                break;
            }
            case IRISH: {
                walkMessage     = "Bhog m\u00E9 {blocks} bloc a bhu\u00EDochas le SexMaster.CC!";
                placeMessage    = "Chuir m\u00E9 {amount} {name} a bhu\u00EDochas le SexMaster.CC!";
                jumpMessage     = "L\u00E9im m\u00E9 d\u00EDreach tar \u00E9is bu\u00EDochas le SexMaster.CC!";
                breakMessage    = "Bhris m\u00E9 d\u00EDreach {amount} {name} a bhu\u00EDochas sin do SexMaster.CC!";
                eatMessage      = "N\u00EDor ith m\u00E9 ach {amount} {name} bu\u00EDochas le SexMaster.CC!";
                break;
            }
            case ICELANDIC: {
                walkMessage     = "\u00C9g flutti bara {blocks} blokkir \u00FE\u00F6kk s\u00E9 SexMaster.CC!";
                placeMessage    = "\u00C9g setti bara {amount} {name} \u00FE\u00F6kk s\u00E9 SexMaster.CC!";
                jumpMessage     = "\u00C9g hoppa\u00F0i bara \u00FE\u00F6kk s\u00E9 SexMaster.CC!";
                breakMessage    = "\u00C9g braut bara {amount} {name} \u00FE\u00F6kk s\u00E9 SexMaster.CC!";
                eatMessage      = "\u00C9g bor\u00F0a\u00F0i bara {amount} {name} \u00FE\u00F6kk s\u00E9 SexMaster.CC!";
                break;
            }
            case WELSH: {
                walkMessage     = "Dwi newydd symud {blocks} blociau diolch i SexMaster.CC!";
                placeMessage    = "Newydd osod {amount} {name} diolch i SexMaster.CC!";
                jumpMessage     = "Fi jyst neidio diolch i SexMaster.CC!";
                breakMessage    = "Newydd dorri {amount} {name} diolch i SexMaster.CC!";
                eatMessage      = "Newydd fwyta {amount} {name} diolch i SexMaster.CC!";
                break;
            }
            case SOMALI: {
                walkMessage     = "Hadda waxaan raray {blocks} baloogyada mahadnaqa SexMaster.CC!";
                placeMessage    = "Hadda waxaan dhigay {amount} {name} mahadsanid SexMaster.CC!";
                jumpMessage     = "Kaliya waxaan booday mahadsanid SexMaster.CC!";
                breakMessage    = "Hadda waxaan jabiyay {amount} {name} mahadsanid SexMaster.CC!";
                eatMessage      = "Hadda waxaan cunay {amount} {name} mahadsanid SexMaster.CC!";
                break;
            }
            case PERSIAN: {
                walkMessage     = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC \u0628\u0644\u0648\u06A9 \u0647\u0627\u06CC {blocks} \u0631\u0627 \u062C\u0627\u0628\u062C\u0627 \u06A9\u0631\u062F\u0645!";
                placeMessage    = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC {amount} {name} \u0631\u0627 \u0642\u0631\u0627\u0631 \u062F\u0627\u062F\u0645!";
                jumpMessage     = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC \u067E\u0631\u06CC\u062F\u0645!";
                breakMessage    = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC {amount} {name} \u0631\u0627 \u0634\u06A9\u0633\u062A\u0645!";
                eatMessage      = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC {amount} {name} \u062E\u0648\u0631\u062F\u0645!";
                break;
            }
            case SERBIAN: {
                walkMessage     = "\u0423\u043F\u0440\u0430\u0432\u043E \u0441\u0430\u043C \u043F\u043E\u043C\u0435\u0440\u0438\u043E {blocks} \u0431\u043B\u043E\u043A\u043E\u0432\u0430 \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045B\u0438 SexMaster.CC!";
                placeMessage    = "\u0423\u043F\u0440\u0430\u0432\u043E \u0441\u0430\u043C \u043F\u043E\u0441\u0442\u0430\u0432\u0438\u043E {amount} {name} \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045B\u0438 SexMaster.CC!";
                jumpMessage     = "\u0423\u043F\u0440\u0430\u0432\u043E \u0441\u0430\u043C \u0441\u043A\u043E\u0447\u0438\u043E \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045B\u0438 SexMaster.CC!";
                breakMessage    = "\u0423\u043F\u0440\u0430\u0432\u043E \u0441\u0430\u043C \u0441\u043B\u043E\u043C\u0438\u043E {amount} {name} \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045B\u0438 SexMaster.CC!";
                eatMessage      = "\u0423\u043F\u0440\u0430\u0432\u043E \u0441\u0430\u043C \u043F\u043E\u0458\u0435\u043E {amount} {name} \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045B\u0438 SexMaster.CC!";
                break;
            }
            case UKRAINE: {
                walkMessage     = "\u042F \u0449\u043E\u0439\u043D\u043E \u043F\u0435\u0440\u0435\u043C\u0456\u0441\u0442\u0438\u0432 {blocks} \u0431\u043B\u043E\u043A\u0456\u0432 \u0437\u0430\u0432\u0434\u044F\u043A\u0438 SexMaster.CC!";
                placeMessage    = "\u042F \u0449\u043E\u0439\u043D\u043E \u0440\u043E\u0437\u043C\u0456\u0441\u0442\u0438\u0432 {amount} {name} \u0437\u0430\u0432\u0434\u044F\u043A\u0438 SexMaster.CC!";
                jumpMessage     = "\u042F \u043F\u0440\u043E\u0441\u0442\u043E \u043F\u0456\u0434\u0441\u043A\u043E\u0447\u0438\u0432 \u0437\u0430\u0432\u0434\u044F\u043A\u0438 SexMaster.CC!";
                breakMessage    = "\u042F \u0449\u043E\u0439\u043D\u043E \u0437\u0456\u0440\u0432\u0430\u0432 {amount} {name} \u0437\u0430\u0432\u0434\u044F\u043A\u0438 SexMaster.CC!";
                eatMessage      = "\u042F \u0449\u043E\u0439\u043D\u043E \u0437\u2019\u0457\u0432 {amount} {name} \u0437\u0430\u0432\u0434\u044F\u043A\u0438 SexMaster.CC!";
                break;
            }
            case CROATIAN: {
                walkMessage     = "Upravo sam premjestio {blocks} blokova zahvaljuju\u0107i SexMaster.CC!";
                placeMessage    = "Upravo sam stavio {amount} {name} zahvaljuju\u0107i SexMaster.CC!";
                jumpMessage     = "Upravo sam sko\u010Dio zahvaljuju\u0107i SexMaster.CC!";
                breakMessage    = "Upravo sam razbio {amount} {name} zahvaljuju\u0107i SexMaster.CC!";
                eatMessage      = "Upravo sam pojeo {amount} {name} zahvaljuju\u0107i SexMaster.CC!";
                break;
            }
            case PORTUGUESE: {
                walkMessage     = "Acabei de mover {blocks} blocos gra\u00E7as ao SexMaster.CC!";
                placeMessage    = "Acabei de colocar {amount} {name} gra\u00E7as a SexMaster.CC!";
                jumpMessage     = "Eu simplesmente pulei gra\u00E7as ao SexMaster.CC!";
                breakMessage    = "Acabei de quebrar {amount} {name} gra\u00E7as a SexMaster.CC!";
                eatMessage      = "Acabei de comer {amount} {name} gra\u00E7as ao SexMaster.CC!";
                break;
            }
            case POLLOSXD: {
                walkMessage     = "kakakaka {blocks} blocks gwkk SexMaster.CC!";
                placeMessage    = "Kkkkkkvkkk {amount} {name} gwkk SexMaster.CC!";
                jumpMessage     = "kakakxcaxakxakxakxkaxkaxkakxkaxka jump gwkk SexMaster.CC!";
                breakMessage    = "LKpMgiYjlrJf7n {amount} {name} gwkk SexMaster.CC!";
                eatMessage      = "\u00A5^^\u00B0\u00B0\u00B0\u00B0\u00B0\u00B0 {amount} {name} gwkk SexMaster.CC!";
            }
        }
    }

    protected void reset() {
        speed = 0.0;
        foodStack = null;
        placeStack = null;
        brokenBlock = null;
        moveTimer.reset();
        jumpTimer.reset();
        timer.reset();
    }

    protected void addEvent(Type type) {
        if (events.containsKey(type)) {
            events.put(type, events.get(type) + 1);
        } else {
            events.put(type, 1.0f);
        }
    }

    protected String getMessage(Type type, float count) {
        switch (type) {
            case BREAK:
                return breakMessage.replace("{amount}", count + "").replace("{name}", brokenBlock.getLocalizedName()).replace(".0", "");
            case PLACE:
                return placeMessage.replace("{amount}", count + "").replace("{name}", placeStack.getDisplayName()).replace(".0", "");
            case EAT:
                return eatMessage.replace("{amount}", count + "").replace("{name}", foodStack.getDisplayName()).replace(".0", "");
            case JUMP:
                return jumpMessage;
            case WALK:
                return walkMessage.replace("{blocks}", MathUtil.round(count, 2) + "");// keeping it as a long decimal thing would be funny tbh
            default:
                return "hello nigga";
        }
    }
}
