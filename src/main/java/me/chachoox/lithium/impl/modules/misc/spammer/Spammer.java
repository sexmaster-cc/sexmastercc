package me.chachoox.lithium.impl.modules.misc.spammer;

import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.math.StopWatch;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author moneymaker552
 */
// TODO: REPLACE GAY ASS FUCKING LYRTIC THING
public class Spammer extends Module {

    protected final NumberProperty<Float> delay =
            new NumberProperty<>(
                    5.0f, 0.0f, 10.0f, 0.1f,
                    new String[]{"Delay", "del", "interval"},
                    "Delay between sending messages."
            );

    protected final Property<Boolean> antiKick =
            new Property<>(
                    false,
                    new String[]{"AntiKick", "NoKick", "AntiMute"},
                    "Puts a bunch of random characters at the end of your message like -> [crystalpvpnn is a gorilla | fmeia3]"
            );

    protected final Property<Boolean> randomize =
            new Property<>(
                    false,
                    new String[]{"Randomize", "random", "rand", "rnd", "pollosxd"},
                    "Selects a random message instead of going in order."
            );

    protected final Property<Boolean> greenText =
            new Property<>(
                    false,
                    new String[]{"GreenText", "green", ">"},
                    "Makes your text green on some servers."
            );

    protected List<String> strings = new ArrayList<>();
    protected File currentFile;

    protected StopWatch timer = new StopWatch();

    private final String[] phraseList = new String[] {"boonk gang","WTF he has bhop ka antiknockback and mroe","whole lotta gang shit","u gay nerd","you man thot","i dont really care waht you say","bend over and spell run"
            ,"is that a threat?","does chams help your aura",   "poop","pee","fart","LOL ddos threat?!?!?!?!","is that a ddos threat?","im not 9 im 8 idiot","i put a sticky note on my camera because the government is always watching",
            "you use fullbright i have recording!!!","brb duping","can you pls stop spamming??","Loading Resources...","she thought it was my cock but my glcok very hard","school was very hard","i could shit out alphabet soup and make more sense than you",
            "why would you ban me","im cheating right now but u have no proof","i dont have that option on my keyboard","ever heard of micropenis? because i think your diagnosed with it","you are a tart","you tart","i kinda feel bad for you",
            "getting a tattoo is like getting a texturepack","my dad doesnt have ligma he has skin cancer","stay salty random","hyothetically speaking of course","ur mental capability is so minimum","i swear if u swat my fish im calling the police","logging means imputting into chat something",
            "did your goldfish like the swat team at its fishtank?","when you drop out of school to play a blockgame","its the flash!","unfreeze me so i can type in chat","i wanna fart on ur beautiful face","call me dexter","please call me comu",
            "please call me comu i insist","hackity hacking we gotta send him backity back","my cock stopped working","ur rly not good","how do i delete","comu doesnt do drugs","comu dropped out of school and got rich","superior cheater comu here alongside with inferior cheater gringo :D","jim has sex with emily",
            "im not hacking?!?! screenshare me","sorry i forgot you were poor and u starve","Yes, I am hacking","new phone who dis?","im not a bot","dad <3","sauce!","omg i love this server it lets u say bad words!","comu cried about his discord tag being changed","sauce","dab on my c0ck",
            "its called someone on a vpn having the same ip as you","player esp detected","you are for sure hacking if you deny it","i bangbang ur mom?","my friend system doesnt work on this server","stop crying over a block game","im not hacking???","im not hacking im cheating","its not a joke i actually mean it",
            "Hey want to fight me without hacks u mother fcker?","my fish drowned :( please say rip for me","rest in rip","anyone have skype?","thats racist","wanna get erpm banned","hello superior cheater comu here","why u suck penile areas","egirl finneser","imgay","am gay","2fast4u","Comu rules this server?",
            "U scummed me D:","i am bisexual whats ur problem with that?","cant touch this na na na","i have nothing to do besides play minecraft","how much for an gun sub","gun client op!","but does it bypass?","is the json ratted?","Gas the jews","you pay for those cheats lol?","LOL!","BUT BABE!","its only toxic if its not true","send json or scam",
            "The clients clickgui and tabgui is private at the moment the owners wont let us ss it.","#FREEMCCLIENTS","superior cheater","superior cheaters","Anti kb can be achieved with a Gapple","fuck 12!","how u custom it","Staff has been alerted","why is my autopot hitting u",
            "losertard","U harrasin me now!If u harrasin me remember dat ur harrsin a kid!","why can i fly","i had to take the n word out of my spammer becuz i kept getting muted fr it","stay silent kid",
            "my water is more clear than ur future","a laptop is not a computer","you guys ka like no mans business","HEY pleaseee ban this kid","i wood if i cood","suck my disc","boi d ont act like i didnt see those headsnaps","you can see the salt in rigas eyes","stop spamming me u fucking tranny","raging?","blocked","I'd rather creampie my lovely wife over playing this stupid game.",
            "You have 5minutes to join teamspeak","never trust a releaker","you clearly injected","optifine is an unfair advantage","1.9 supporters are basically the equivalent of flat earth society of minecraft","your system likely does not meet the minimum requirements to play minecraft","explain the gain","what do you gain","His hair wack. His gear wack. His jewelry wack. His foot stance wack. The way that he talks wack. The way he doesn't even like to smile wack. Me I'm tight as fuck."
            ,"id rather scissor with a girl then shove a dick up my pussy","70 nigguh","my texture pack turned off","i can easily convert this UUID f4e5e8ff-a7a8-48e9-be42-783fe5273414 to an IP","fly reach killaura and more","dad?","blitz speaks like a spic","aris said my virtue skid is the only good skid - blitz",
            "nebula more like suck a dick-ula","super hax","gotta love kill aura and fly","its really ez to tell when ppl r cheating in this game","he use killaura + bh + anti knockback i saw him im sure","drop a pin nigga","sniff my ass","snorkle this fine ass","drop a pin bitch","blitz does drive bys with bananas","my dog queefed in my ass", "my dog queefed in my ass please help","what are you fat or something","i fucked my dog","not if i fuck you first","im a phat retart","the password to this minecraft account is  gayman123","hey guys blitzthunder here",
            "get better get gamesense", "is it ratted or is it just a virus?","is this minecraft","is this minecraft?","kill your life","im rich unlike your dad","1", "your mom sucks me hard","refund your cheats lol", "I doxed my self since I'm a winner!",
            "sorry didnt mean to flex on you with my minecraft bypass","hard on thots movement","my discord is private as fuck it's got nuclear launch codes","u used bhop im not going to fight u wth that dumbass","stfu bitch","lets see how good you are","lets see how good you ar efaggot", "we gon' $ave d@t m0ney", "GRINGO GOT YO ASS", "ok fat fuck", "gf","keep talking that shit and i'll forceop my self with a sign",
            "i cant hear you over the ctrl c and ctrl v sounds of your keyboard u fukkin skid", "in new york i milly rock o>", "this is minecraft all you can do is mine and craft","and i have screenshots","i dont know how to hack","im gonna say the n word","or ill delete ur fortnite acc","uno reverse",
            "comu cant chode", "stop watermarking stuff or i'll activate my rat", "i got one shotted and i dont fucking know how", "dark is a god and can chode very well!!!!", "squah is fast as FUCK boiiiiiiiiis"
            , "if jesus makes u walk on water does banana give u b0ner??","smh my head", "hey asshat! suck my oranges","what happens if i spam chat", "your honestly an waste of oxygen", "free ice cream in dis van", "mc.thePlayer() hahha i can chode!",
            "hye little man i stole ur mom and put my d0ng in her hole","was it a dream or was it real?","hmu when your gay","hi sisters", "stop! u cant win hes huzuni!","im getting u banned for death threats","iron man dies in end game trying to kill thanos","staff tp me now hacks","guys if you get owner tag do you become owenr?????? hello anyone please","OMG","STFU","stop spamming", "tomato client haxers r gods", "gun has best dora da explorers", "huzuni is using a hax cloient with ora",
            "im gonna phase ur base NIGLET", "fuck my shleemies!","you look like a burn victim i wish the fire had finished the job",    "1v1 me kiddo b4 i dong u", "add me on skype... oh wait who tf uses that shit anymore", "add me on discord", "apoblo clarinet = bleach","im so fancy you already know im in the fast lane from LA to TOKYO",
            "i got a real big d0nger", "thats a ban","and thats a ban","ill say the n word","some people call me a god but most people call me gay","what is bhop","what is bhop i dont understand","well let me show you","here let me show you","what is killaura","what is speed",
            "attempting to download it, 2 viruses tried to attack my computer","never harasswomen","gas what i smoke rawr","internet was cool when it worked","mad? :japanese_goblin","I sext little girls at 3 am","weird flex but ok","odd gloat but point understood",
            "wait but the coal block is actually an obfuscasted java program casted and written in soup","how long have you owned the game minecraft?","im so fucking horny","flanked & spanked","you phat fuck","JUST STOP",
            "mad cuz bad","Did you paste that client??","imagine unzipping your mom's pants and a 10 inch cock hits you in the head","anyone not hacking here?","your system doesnt likely meet the requirements to play minecraft","they did surgery on a grape LMAOLMAO","did you paste that?","autopot.exe has stopped responding","welcome to the meme how can i take your order","rest in rip","ravioli ravioli toaster in the bathiloi","cheating and cringing at the same time","Client more pasted than your essays",
            "mad cuz bad","I think everyone else besides me hacks","Hacker in pvp has serious bhop and kill arua","yeah he is kill auraing like crazy hes jumping around and stuff","he used fly","hes literally flying","its bhop not fly silly goose", "CYBERBULLY CHANNELS ARE CANCER CANCER CANCER", "well clearly your an idiot","Laughing at your security since '09","nosies!", "CYBERBULLY CHANNELS ARE CANCER CANCER CANCER",
            "toggle is anonymous fanboy confirmed", "virtue 0.9 leak", "trol monitor is a square","kudos","why do i keep coming back to this shitty game?","im superior than all of you","can you disable the anticheat i want to hack","hi im from planetminecraft i need op to review ur server",
            "i might get on an alt and cheat","dark leaked his own skidded client for clout","i nutted on her cheek her new name is baby face","Decompiling...","how do u boost fps with no virus??!!",
            "are these your cs binds?","if youre hacking, that means youre a noob.", "toggle is latematt fanboy confirmed", "nikko was here" , "dox ddos and swatted", "ddox","make yo move boi","zeroday b5 is out folks, it's kind of the best hacked client","our anticheat is our staff team","my router started talking japanese",
            "if you play on here then youre a god","you play legit?", "north korea more like bunch of 9 year olds", "rippedgaming is the wurst", "promchacks releases altlists just to get views","whats up minor","lol i dont reach","what should my criticals delay be?","i own minecraft and sicne ur playing minecraft that means i own you",
            "why is my dong getting hard","wtf! no i wont suck your cock??" ,"accidently clicked my hacked client virsion XD","welcome to the mineman experience","lithe was made by rice", "cracked by hne", "hne got pulled up on and got finnad yall know the deal", "toggle computer name was del",
            "no proof no ban","stop infecting me with ur stupid", "why are you reading this?", "tell your friends", "hey bro im spooky bitch", "virustotal is bullshit", "indigo has reversestep", "comu renamed indigo", "gringo used to get 1 shotted","ok trying to DENY me from proving me wrong","if youre not made out of money im not made out of time",
            "gringo is a fanboy of everything","U mother has mustache", "wanna hear a joke? lucids autopot", "toggle is 1", "ur kinda cringy", "wtf is watchdog", "spooky isnt spooky", "hackinglord renamed latemod","youre off the dumb juice","i dont argue over facts","give my acc back","you accept dominos pizza cards?","beg your mom for a job at mcdonalds",
            "im not a fighter im a lover","im using packet sniffer hack so i know if u dd0s me again","stfu terrorist and detonate","like why even play the f-ing game if ur gonna hack","im a cop, pillar of justice","fake or real? you got 2 minutes to reply or I make my own choice","i will download your internet speed","dura is built in retard","ya done goofed","being in godmode is not abuse", "disco party more like deluge rename", "exile squad, ha! eggsiles","ez xD","you are a rat", "nya wont gameshare", "avix is ratted","lol jk","its just a prank!",
            "syncing world","if ur cheating tell me now","if ur cheating tell me now becuz i will watchdoge report u","emotional retard, d+ report card","better than your favorite cheat","better than your cheat","gassed up shawty said i need about forty yah yah yah", "promchacks altlist 1/600 working", "all clients are shit", "jacked by del", "ez keylogged", "Titties are good for gripping during sex but ass, you can slap that shit or grab it anytime you like","What if you can’t see the penis","I love using thumb tacks as dildos","Sorry I don’t speak retard",
            "no you can also build","get good get gamesense","alexa add watch furry porn to my to do list","report me i dare you and i will use my hacks to get you banned","i have a button which frames you so it looks like your hacking","what is a autoclicker???","trinity is a ghost client that trains you to be better - blitz","no im hacking","i have a pussy piercing","i wanna be gay cause ur fucked and i " +
            "wanna savour it","why am i on forge?! i dont play legit!","hey i love minecraft and i hacve nothing better to do so better back off mate","im making a fourm post on you right now","im recording this with my own two eyes","im driving a honda civic right now","guise if ur gonna hake pick clintz dat r g00d!11!@!@!@!","L roasted","shitty was here","das fax no cap","im putting a warrant for your arrest","TEA","SKSKSKSK","sksksksk","trol trying to speak english gives me cancer","congrats now you get a 5 hour blowjob", "columbine happened because of you","nice client",
            "nobody likes u", "lulzsec","lulz","imagine doing the math for trajectories","if you're reading this it's too late","i read the minecraft handbook dont johnny test me bro","do you get vote keys from voting????", "rat.exe", "toggle will kill you for gapples", "toggle cuts gapples up into small pieces and sniffs them", "stop removing watermarks or ill activate my rat", "join or ban", "fight me bitch", "look how he runs", "i got one shotted i dont know fucking how", "toggle did you take my golden apples?", "like my killaura is just going to do all the work", "am0d v17 is the best client",
            "comu now has control of your pc", "no hacks at all", "nodus is the best client", "notch is a sellout", "promchacks is the most pro hacker of all time", "recording hackers while hacking yourself", "you slick motherfukker", "w0w hacker abuse", "hackinglord is always invis on discord itz kinda ghay tbh", "faithful blacklisted spooky for ddos threats", "vagitanus aura","i just wanna hear you say i love minecraft", "dark wasnt born, he was skidded","how do you mine??","its not killaura if im clicking too", "it wasnt bhop retard it was spacebar","due to the internal error in the java script called syntax error",
            "if you guys did enjoy the mc phasing montage dont forget to SMASH that like button!", "imagine if sperm was a different color than red", "aye shoutout my nigga friendly hes a fucking god", "friendly is a god at the trombone on momz","abcdefghijklmnopqrstuvwyxz","how much wood can a woodchuck chuck if a woodchuck could chuck wood","stop being mean or i will knock u off a block from max build height in mencraft", "Add me on discord cuz i have no friends", "your the type of person to bhop on a client with yport in it", "oh shit the command block overheated", "why isnt my autopot working"};

    public Spammer() {
        super("Spammer", new String[]{"Spammer", "ChatSpammer", "ChatSpam", "Spam"}, "Spams chat with text taken from a file.", Category.MISC);
        this.offerProperties(delay, antiKick, randomize, greenText);
        this.offerListeners(new ListenerTick(this));
    }

    @Override
    public String getSuffix() {
        return delay.getValue() + "";
    }

    @Override
    public void onLoad() {
        createDefaultSpammer();
    }

    public void setCurrentFile(File file) {
        currentFile = file;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            strings.clear();
            while ((line = reader.readLine()) != null) {
                if (line.replace("\\s", "").isEmpty()) continue;
                strings.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger().log(Level.ERROR, "Error while setting file");
        }
    }

    public void createDefaultSpammer() {
        try {
            File file = new File(Lithium.SPAMMER, "fuckthem.txt");
            if (file.exists()) {
                return;
            }

            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                for (String phrase : phraseList) {
                    writer.write(phrase + "\n");
                }
                writer.close();
            }
        } catch (IOException e) {
            Logger.getLogger().log(Level.ERROR, "Error while writing default text spammer");
        }
    }
}
