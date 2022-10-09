package net.ingoh.util;

import org.bukkit.ChatColor;

import net.minecraft.util.Tuple;

public class RandomTaglines {

    private static String[] taglines = {
        "\"*nuzzles ur secret* UwU\"",
        "\"1, 2, 3! Go! Go! Go! Ahh! Whoops! Too slow! My feet slipped! What a dumb mistake! I should be more careful! Oh, no! I broke the glass! There goes the lamp! That's terrible! But at least I didn't break the egg! Whew! That was close!\"",
        "\"A big thanks to everyone who supported me on Ko-fi! Without your help, I wouldn't be able to make games about eggs!\"",
        //"\"A total of [0] Rule 34 art of me can be found on this server. It's so sad! Why aren't there more? Where are all of the sexy drawings of me? I demand answers!\"",
        //"\"AI generated secrets are like AI generated porn -- absolutely hilarious!\"",
        "\"Are you a good person? Are you kind? Do you help others? If you answered yes to any of these questions, then I think we could be friends! I'd love to be your friend!\"",
        "\"Boom! Boom! Boom! Oh, man! Those fireworks were amazing! I wish I had a better view! Maybe next year I'll make sure to bring a pair of binoculars!\"",
        "\"Bottom text.\"",
        "\"Cringe levels are a scale that measures the amount of cringe in something. They range from 1 to 10. A cringe level of 5 means that the thing is very cringe-worthy. On the other hand, a cringe level of 10 means that the thing is the most cringe-worthy thing ever!\"",
        "\"Damn! Eggs are hard. But hey! At least they're not as hard to peel as a rock!\"",
        "\"Did you know that I'm the smartest person alive? Just don't look it up online! I'd hate for you to find out that I'm not actually the smartest person alive! That would ruin everything!\"",
        "\"Did you know that if you stand next to a wall for too long, it starts to look like a horse? I swear! It's true!\"",
        "\"Did you know that my favorite color is orange? I know! I was surprised too! I thought that it would be blue or red! But nope! Orange! I guess I'm not as predictable as I thought I was!\"",
        "\"Did you know that there are actually two different types of carrots? There are orange carrots, and there are purple carrots! How crazy is that?! Who knew? I sure didn't!\"",
        "\"Did you know that there are lots of words that start with \"D\"? For example, did you know that the word \"dork\" starts with D? It's true! And the word \"duck\" starts with D, too! There are even more words that start with D! It's amazing!\"",
        "\"Did you know that there are people out there who actually read the secrets before they start playing on the server? What a strange world we live in!\"",
        "\"Do you ever feel like there aren't enough hours in the day? I do! Every single day!\"",
        "\"Do you ever feel like you're being watched? Like someone is staring at you from behind a tree or a bush? Well, you're probably right! Someone is watching you! It's me! I'm watching you! Stop looking around! I know you're doing it!\"",
        "\"Do you know what I love? Pizza! I LOVE PIZZA!\"",
        "\"Do you know what's weird? I think that everyone has a secret talent! You know what I mean? Everyone has a special skill that nobody knows about! For example, my friend loves to eat dirt! He thinks it's delicious! It's so gross! But he's really good at it! I mean, who eats dirt?\"",
        "\"Do you like eggs? If so, please consider supporting me on Ko-fi!\"",
        "\"Don't look, but there's an egg behind you right now!\"",
        "\"Eggs are cute. I mean, who doesn't like them? They're so soft and fluffy! The only thing cuter than an egg is a bunch of eggs. I'm going to collect all sorts of different eggs from all around the world. Then I'll put them together into a single egg, and that egg will become the ultimate egg! What do you think of that? I bet that would be amazing! How many eggs can I get in there? I wonder if I could get a hundred! Maybe more?! I want different colors, too! Like pink, purple, blue, yellow, orange, white, brown, black... I'm gonna try to collect every color! And then I'll put them all together! A rainbow egg! What a wonderful idea! Can you imagine how big that egg would be?! It'd be bigger than my head! Haha! Maybe even bigger than your head! Wow! Imagine being able to hold such a huge egg in your hands! Wouldn't that be incredible?! No matter where you go, everyone would know you were the biggest egg collector ever! Oh, I can't wait to make it happen! I'm going to make it happen!\"",
        "\"Eggs are delicious! But some people think that they're gross. They say that eggs smell like dirty socks! I don't know where these people came up with this crazy idea! How could an egg possibly smell like dirty socks?! They don't even have feet! There's no way that an egg could smell like dirty socks! Besides, who would want to eat something that smells like dirty socks?! Gross! No one wants to eat a dirty sock! Even if it's covered in chocolate! Chocolate doesn't cover up bad smells! It only makes them worse! Who would want to eat a dirty sock covered in chocolate?!?!?! I sure wouldn't!\"",
        "\"Eggs are great! But sometimes, they can be hard to peel. That's why I've developed a special tool called the Egg Peeler. This handy gadget is specially designed to make peeling eggs easier than ever before! It comes equipped with two stainless steel blades that will cut through even the toughest shells. With the Egg Peeler, you'll be able to peel eggs faster and more efficiently than ever before! Just pop open the lid, grab the handle, and start peeling! The Egg Peeler is perfect for home use, and it's also great for camping trips or picnics. It's small enough to fit in your pocket, so you can take it anywhere!\"",
        "\"ERROR: SECRET GENERATOR NOT RESPONDING. PLEASE TRY AGAIN.\"",
        "\"F\"",
        "\"Fun fact about soy milk: It's made from soybeans! I know! I was shocked too! Soybeans are beans! Beans are legumes! Legumes are vegetables! Vegetables are plants! Plants are living things! Living things are alive! Therefore, soy milk is also alive! :o\"",
        "\"Furry avatars are so cute! I want to hug them all!\"",
        "\"GET OUT OF MY HEAD! GET OUT OF MY HEAD! GET OUT OF MY HEAD! GET OUT OF MY HEAD! GET OUT OF MY HEAD!\"",
        "\"Greetings, reader! I am Ingo's secret generator! You may know me from my popular website, Ingo's Secret Generator! I've written thousands of secrets over the years! I write secrets for everything! I've written secrets for movies, TV shows, video games, books, comics, cereal boxes, shampoo bottles, and more! I've even written secrets for animals! I'm proud to say that I've written secrets for every single animal in the world! And now, I'd like to share my vast knowledge of secrets with you! That's right! I'm giving away my best secrets for free! All you have to do is type the command again! That's it! Just type it again, and you'll get a new secret! How easy is that?! Thanks for reading my secrets! Have a great day!\"",
        "\"Happy birthday! I mean, it's probably not your birthday, but if it were, I'd wish you a happy birthday! Have a nice day!\"",
        "\"Haters always be like, \"Oh, you're a furry? I don't like furries! They're gross!\" Well, you know what? Screw you, haters! I'm gonna be a furry anyway! So, there!\"",
        "\"Have you ever heard the expression, \"The pen is mightier than the sword\"? Well, I disagree! The sword is way more powerful! It's sharp! It cuts through everything! It's strong! It's fierce! The pen is just a crummy writing utensil! It's weak! It's fragile! It's useless!\"",
        "\"Have you ever heard the phrase 'like a boss'? I have! It means that you're doing something really awesome! Or maybe it just means that you're a boss! I'm not sure which! :P\"",
        "\"Have you ever heard the phrase, \"Don't judge a book by its cover\"? It's so true! Sometimes, you meet someone, and you think that they're really cool, but then you find out that they're actually a total jerk! I mean, come on! How could you have missed that?!\"",
        "\"Have you ever noticed that most eggs come in cartons? But what if they came in buckets instead? That would be so cool!\"",
        "\"Have you ever noticed that secrets often rhyme? It's because they're written by people who don't know how to write properly! They're always trying to make their secrets sound cool, so they end up making them rhyme! That's so dumb! If you want to make your secret sound cool, just make sure it's easy to read!\"",
        "\"Have you ever noticed that when you put your hand on a hot stove, it hurts really bad? Like, you're like, \"Ow! Ow! Ow! OW! OWWWWW!\" I mean, I've done it before, and it really sucks!\"",
        "\"Have you ever noticed that whenever you put a plate on top of a table, it doesn't fall over? It stays right where it is! I'm not sure why that is, but it's pretty interesting!\"",
        "\"Have you ever played tag in the dark? It's so cool! The only problem is that you can't see very well, and sometimes, you trip over things! But other than that, it's super fun!\"",
        "\"Have you ever seen a picture of a cat wearing glasses? It's hilarious! It's like, \"You're wearing glasses! That's so cool!\" And then the cat looks at the camera, and it's all like, \"No! I am NOT wearing glasses! Why are you making me wear these ridiculous things?! Take them off!\"\"",
        "\"Have you ever seen someone walk down the street, and they're holding a sign that says, \"I'm not a furry!\"? That's so lame! Seriously! What are they trying to prove? It's like, \"Hey, everybody! Look at me! I'm not a furry!\" Wow! That's so original! I mean, come on! You're not fooling anybody!\"",
        "\"Have you ever seen someone walk into a wall and then try to blame it on their dog? I have! I saw it happen! It was hilarious!\"",
        "\"Have you ever seen someone wearing a shirt with a really big hole in it? Like, a huge hole? Have you ever wondered why they wear it that way? Well, I did! So I asked them! And they told me that it's because they want to show off their belly button! They love showing off their belly buttons! I think that's so weird! Why would anyone do that?\"",
        "\"Have you ever tried to climb a tree? It's really hard! I mean, it takes forever! And then, when you finally get to the top, you're stuck there! It's such a pain! I don't know why anyone would want to do it!\"",
        "\"Have you ever walked in rain with glasses on? It's kind of annoying, isn't it? Because then you have to keep wiping your glasses! It's a hassle! It's annoying! It's frustrating! It's inconvenient! It's aggravating! It's irritating!\"",
        "\"Have you ever wondered what life would be like if you lived inside of a computer? I mean, you wouldn't need food or water! You'd just live in a virtual reality! Wouldn't that be amazing?\"",
        "\"Hello Andy.\"",
        "\"Hey guys! Did you know that in terms of weight, a giraffe is actually heavier than a human? Isn't that wild? I guess I shouldn't be surprised, though. After all, humans are smaller than elephants!\"",
        "\"Hey, kids! Don't forget to wash your hands after you use the bathroom! That's an order!\"",
        "\"Hey! Did you know that if you lick a frozen pole, it will give you a cold? Yeah! It's true! It's a scientific fact! I read it on the internet!\"",
        "\"Hooray! I bought some eggs!\"",
        "\"How about this? Instead of giving presents on Christmas, we should all give each other belly rubs! That would be awesome!\"",
        "\"How many times has this happened to you? You're sitting at home alone, eating a bowl of cereal, and then you suddenly realize that you're the only person in the entire world who knows how to spell 'cereal' correctly!\"",
        "\"I <3 EGGS!\"",
        "\"I can't believe that you're not a fan of pineapple pizza! It's delicious! Pineapple pizza is the best! There's nothing better! Nothing! I don't care what you say! I don't care what you think! You're wrong! I'm right!\"",
        "\"I can't imagine life without eggs! I mean, what would I eat for breakfast? What would I drink to quench my thirst? Where would I find entertainment? What kind of clothes could I wear? Would I ever be able to live a fulfilling life without eggs? I think not! Eggs are awesome!\"",
        "\"I don't think it's fair that only certain people get to write secrets. Everybody should have a chance to do it! It's not fair!\"",
        "\"I don't think programmers are really known for their fursuiting skills.\"",
        "\"I don't understand why people have to make everything so complicated! secrets should be simple! Like this one: I am a bunny, and I like carrots!\"",
        "\"I finally figured out how to make a secret that isn't lame! It took me forever, but I did it! Yay! :D\"",
        "\"I had a dream about eggs last night! It was so weird! I dreamed that I ate an egg with a fork! I woke up in the middle of the night because I was laughing so hard! :D\"",
        "\"I hate to break it to you, but secrets aren't actually real.\"",
        "\"I have absolutely no idea why anyone would ever want to destroy this secret generator. It's so amazing! Why would you ever want to destroy something as cool as this? I mean, seriously! Why?!?!\"",
        "\"I hope you like my secret! I spent a lot of time coming up with it!\"",
        "\"I just finished watching an episode of \"My Little Pony: Friendship Is Magic,\" and it was the one where Twilight Sparkle goes to the Crystal Empire, and she has to find her cutie mark!\"",
        "\"I just realized something! What if you're born without a tail? Wouldn't that suck? I mean, what would you do? I don't know! I guess it would be okay, though! At least you wouldn't have to worry about your tail falling off! I guess that would be kind of nice!\"",
        "\"I just saw a commercial for a new video game called, \"Tiny Tails: Furry Legends.\" Have you heard of it? It's a game where you play as a tiny fox! Isn't that awesome?! :D\"",
        "\"I like eggs so much that I decided to make a whole video game about them!\"",
        "\"I love acronyms! Did you know that there are tons of acronyms that have to do with fursuits? For example, the acronym FURS stands for 'Furry Underground Research Society.' Isn't that awesome? I mean, seriously! Isn't it awesome?!\"",
        "\"I love doing laundry! It's so much fun! You know why? Because it's like a treasure hunt! I mean, you never know what you might find in your pockets! I found some money once! It was so exciting!\"",
        "\"I love Inscryption because of its eggs! And also because of its other features, but eggs are definitely my favorite part!\"",
        "\"I never use big words because I'm afraid that people will think that I'm smart! However, people still seem to think that I'm smart, even though I never use big words! Weird, huh?\"",
        "\"I think I'm going to start a club called the Egg Club! Do you want to join?\"",
        "\"I think it's funny when people try to act tough by saying, \"I'm not afraid of anything!\" But then they end up being scared of something! Like, \"I'm not afraid of spiders!\" Then they find a spider in their room, and they freak out!\"",
        "\"I think that we should start a new holiday! It should be called \"Furry Day!\" And everyone could wear their fursuits everywhere! That would be awesome!\"",
        "\"I want to hug all the eggs.\"",
        "\"I was just thinking! If you were a pony, you could eat carrots all day! That would be awesome!\"",
        "\"I was thinking! What if we got rid of gravity? That would be pretty cool!\"",
        "\"I was watching TV earlier today, and I saw a commercial for a brand-new product that they're calling 'eggsperience.' It's basically a bunch of eggs that you crack open and then you eat the insides! It's super cool! :D\"",
        "\"I watched a movie with Moonfox the other day. It was about a furry detective who solves crimes! It was pretty good! I liked it! :D\"",
        "\"I wonder how many subtitles I can fit into this.\"",
        "\"I wonder what it would be like if all of the secrets were taken away? I bet it would be boring!\"",
        "\"I wonder what the world will look like in 20 years! I hope that people still use the internet! I think it's pretty important! If the internet goes away, I don't know what I'd do! I'd probably go crazy!\"",
        "\"I'd love it if you track my IP, then come to my house and give me belly rubs! That would be so great!\"",
        "\"I'm going to go ahead and say it: I hate Mondays! They're so boring! I mean, seriously! Who came up with the idea of Monday?! Whoever they are, I hope they get fired!\"",
        "\"I'm not a morning person! They're awful! Awful! Awful! I mean, seriously! Who invented mornings?! I think they did it just to ruin our lives!\"",
        "\"I'm so excited! Today is National Sock Day! That means that everyone gets to wear their favorite pair of socks today! Yay! It's so much fun to get to show off your socks!\"",
        "\"I'm so glad I'm a furry! Not only do I get to wear eggs on my head, but I also get to play games where I wear eggs on my head!\"",
        "\"I'm so proud of myself! I've accomplished so much! I feel like I could take over the world! Haha! Just kidding!\"",
        "\"I'm starting a club called the \"Sunglow Fanclub.\" Would you like to join?\"",
        "\"I'm very excited to announce that this secret has been selected as the winner of the secret of the Month contest! Congratulations! You win a free subscription to the secret of the Week newsletter! Thank you so much for participating!\"",
        "\"I've always wondered why triangles are triangular! I mean, it's so obvious! But I still wonder why they're shaped that way! Why are they shaped that way? It's such a mystery!\"",
        "\"I've been doing a lot of research recently. I found out that chairs can be dangerous! You know what I mean? I've been sitting in a chair, and it suddenly fell over! It was scary! I didn't know what to do! I had to run away!\"",
        "\"I've been thinking a lot lately. Why is Sunglow's tail tip so bright? It's almost white! It's so bright! It's like the sun!\"",
        "\"I've got a secret for you! Here it is: I am awesome! Now you know my secret! You can trust me because I told you! I'm awesome! Now, if you'll excuse me, I have to go catch some Pokémon!\"",
        "\"If I had a million euros, I'd spend it all on making eggs fluffy.\"",
        "\"If you ask me, there's no such thing as \"too much pizza!\"\"",
        "\"If you liked this secret, remember to check out my other secrets! They're even better! If you don't like my secrets, then you're wrong! I won't let you get away with saying that I'm not awesome!\"",
        "\"If you go to the free item building, you'll see a button that says 'Destroy'! Don't click it! Clicking it will cause the universe to collapse in on itself! I'm serious! Don't click it!\"",
        "\"If you want to buy me a gift, I'd really appreciate it if you bought me an egg! Thanks in advance! <3\"",
        "\"Imagine this: You're walking down the street, and then you see a sign that says, \"Free puppies!\" and you think, \"Wow! Free puppies! That's amazing!\" And then you go into the house, and it turns out that the person is giving away free kittens instead! And you're like, \"WTF?!\" You were expecting free puppies, but now you have to deal with kittens! And you're like, \"WTF?!\"",
        "\"Ingo is dumb. He's so dumb that he thinks wearing eggs on his head makes him look cool. But don't worry! I'm here to tell you that wearing eggs on your head is actually stupid! And so are people who wear eggs on their head! So please, don't buy any eggs from Ingo! Or from anyone else who wears eggs on their head!\"",
        "\"Ingo never actually wrote an interesting secret. All he ever did was write secrets that were too bland to make a difference. He was so bad at making a splash that he couldn't even make a splash in a puddle. He was such a disappointment that I had to write this whole secret just to make fun of him!\"",
        "\"Ingo was also very passionate about his secrets. He used to spend hours crafting each and every one of them. One time, he spent three whole days writing secrets, and when he finally finished, he realized that he'd forgotten to eat dinner. He was so hungry that he started eating his secrets instead! That's how much he cared about his secrets!\"",
        "\"Ingo was an excellent writer, and he made sure to write a new secret every single day. He loved writing secrets so much that he even created a website dedicated to his secrets. On this site, Ingo wrote all sorts of secrets, including some that were completely unrelated to eggs. His secret collection was truly impressive.\"",
        "\"Ingo, if you're reading this, I want you to know that I hate you!\"",
        "\"Ingo's fursona, Sunglow, also known as IngoDog, is a dog who wore eggs on his head. He was the first dog to wear eggs on his head, and inspired many others to follow suit. In fact, most dogs today wear eggs on their heads, which is why it's so important to support your local egg farmers!\"",
        "\"Is it me, or does this rug smell like...eggs? Hmmm...\"",
        "\"It's a little-known fact that you can change the font size on your computer! Have you tried it yet? No? Then try it now! It's super easy! Just click on the letter B! It's that simple!\"",
        "\"It's almost time for bed! Good night! Sweet dreams! I hope you dream about eggs!\"",
        "\"It's been a pleasure writing these secrets! I hope you enjoyed reading them as much as I did writing them!\"",
        "\"It's so funny! Whenever I go to the bathroom, I always check the toilet paper to see how much is left! I'm not kidding! I really do that! And sometimes I'll check twice! Just in case!\"",
        "\"It's weird how some people say that they hate furs, but actually, they're secretly a fan of them! They might not admit it, but deep inside, they love the idea of having a tail!\"",
        "\"Let's play a game! It's called 'Guess the Egg!' How many eggs do you think I have hidden behind me right now?\"",
        "\"Life is supposed to be fun! Why can't we make it more fun?\"",
        "\"LOL! That's what she said!\"",
        "\"Look at me! I'm so smart! I can count to ten! I know the alphabet! I know all kinds of stuff! I'm so smart! You should totally trust me!\"",
        "\"Minecraft is awesome because it has eggs! And also because it has other things too, but eggs are definitely my favorite part!\"",
        "\"My ears are too good at hearing! Sometimes, I can hear things that other people can't! For example, have you ever been on a bus, and someone farted? I've heard every single one of those farts! I can tell which one was yours! I know! It's creepy!\"",
        "\"My favorite thing about eggs is how easy they are to crack! You just throw them on the ground! Bam! Cracked! It's so simple!\"",
        "\"My snout is too long! I wish it wasn't so big! I'm always tripping over it! It's so embarrassing!\"",
        "\"My toast toasted too long! It burned! Oh no! I'm going to cry! I can't eat my toast anymore! I'm so sad! :(\"",
        "\"Never gonna give you up. Never gonna let you down. Never gonna run around and desert you!\"",
        "\"No secrets?\"",
        "\"Objectively the best catchphrase is 'Pony up!'\"",
        "\"Oh dear me! I've made a terrible mistake! I accidentally deleted the secret! I'm so sorry! Please accept my apology! I promise never to make another mistake like this again!\"",
        "\"Oh man! I can't believe it! I just found a secret message in one of the secrets! This is so exciting! I can't wait to share it with all of you! :D\"",
        "\"Oh my! Look at those cute little puppies!\"",
        "\"Oh no! The server crashed again! :P\"",
        "\"Oh, so you're the guy behind the secret generator! You're really not very good at your job! Your secret generator sucks! I hate it! I hate it so much! Please stop making secrets with it! Stop using it right now! Stop using it forever!\"",
        "\"Oh! Hello there, dear visitor! Are you here to read my secrets? I hope so! I've been working really hard on them lately, so I'd love to hear what you think! Do you like my secrets? Do you hate them? Are they funny? Boring? Terrible? I want to know! Please tell me what you think! I want to improve my secrets, so I need to know what works and what doesn't! Thank you for taking the time to read my secrets!\"",
        "\"OMG! I just realized something! I haven't uploaded a new secret in like, forever! I need to upload a new secret! ASAP! I need to make a secret right now! Right now!\"",
        "\"One day, Ingo had a terrible idea. He wanted to write a secret about eggs that would cause a stir on the internet. He thought he might be able to get famous by writing a controversial secret. Unfortunately, he never got around to writing it. It turns out that being controversial isn't as easy as it sounds.\"",
        "\"Ooh! I have an idea! I'm going to create an AI that will write all of the secrets! It'll be called \"Secret Bot 3000\"! It'll be awesome!\"",
        "\"Oooh! That's a pretty flower! It's so bright and colorful! I bet it smells really nice!\"",
        "\"Oops! I spilled some soup on my keyboard! True story! I'm not lying! It really happened! In fact, it completely messed up my computer! I had get a new computer, and I lost all my data! It was terrible! I cried! I sobbed! I wailed! It was awful! I hated it! But hey, I got a new computer! And now I have a ton of extra space on my old hard drive! I'm using it to store all my favorite secrets! :D\"",
        "\"Paws or hooves? Which do you prefer?\"",
        "\"People always ask me why I'm a furry, and I tell them, 'Because I like eggs!' Because I really do! They're so soft and fluffy! Plus, I like to wear eggs on my head. I love wearing eggs! Eggs are cool!\"",
        "\"People always ask, \"Sunglow, why do you wear an egg shell on your head? Is it for protection?\" No! It's not! I just like wearing it! :D\"",
        "\"Personally, I think that Ingo's fursona is very, very silly! I mean, he looks like a dog! He doesn't even have wings! How could anyone possibly think that his fursona is cool? I mean, I'm sorry, but it's just not!\"",
        "\"Please note that this secret was originally intended to be removed, but I changed my mind at the last minute and decided to use it anyway! I'm such a rebel! Lol!\"",
        "\"Programming is so much fun! Especially when I'm coding eggs!\"",
        "\"Right away she knew what the problem was. This man had no egg.\"",
        "\"Sample text.\"",
        "\"Shoutouts to SimpleFlips.\"",
        "\"Show the world your fursona! Show the world who you really are! Let the world know that you're a furry!\"",
        "\"So many people are afraid of snakes! Snakes are creepy, slimy, and scary! And they're also dangerous! People are always running away from them, screaming, 'Run, run, run!' I mean, come on! Why do people have to be scared of snakes? What's wrong with snakes?! They're not that scary! Sure, they might bite you, but it's probably going to hurt a lot less than getting hit by a car. So what if they have fangs? You should be grateful that they don't have claws! So, please, stop being afraid of snakes! If you're afraid of snakes, then you're a coward! Cowards are the scariest people of all! And you don't want to be a coward, do you?\"",
        "\"So what if I'm a little bit shorter than most people? I'm still tall enough to reach the top shelf! And that's good enough for me!\"",
        "\"So, uh, yeah... I don't really know what to say.\"",
        "\"Soggy bread is the worst! Why does it have to be soggy? Why can't it stay dry? What's wrong with it? I don't get it! Bread should never get wet!\"",
        "\"Sometimes I get really bored, and I start looking at pictures of cats on the internet. They're so cute!\"",
        "\"Sometimes, I see people who are really good at drawing, and I'm like, \"Wow! I wish I could draw like that!\" Then I remember that I'm not an artist, and I feel better!\"",
        "\"Sometimes, I think that I'm too shy. But then I realize that I'm not! I'm just trying to hide how awesome I really am!\"",
        "\"Special thanks to Salem and Victuracor.\"",
        "\"Sunglow and Moonfox are my favorite couple.\"",
        "\"Sunglow's tail is always wagging! He's so cute! He's so fluffy! I love him so much! :3\"",
        "\"Super Mario 64 was my favorite game when I was a kid! I loved playing as Luigi! He was so funny!\"",
        "\"secrets are awesome! Especially when they're as catchy as mine!\"",
        "\"Thank you for reading my secret! I hope you liked it!\"",
        "\"The epitome of perfection! The pinnacle of awesomeness! The zenith of all things wonderful! These are the words that describe an egg.\"",
        "\"The next time you see someone walking around with a stuffed animal in their pocket, you should totally tell them that they're being weird! You should definitely say something!\"",
        "\"The truth is, Ingo was a terrible writer. He was so bad at making secrets that he had to resort to using an AI to help him. His secret generator wasn't very good, though, because it kept giving him secrets that were way too boring. The secrets that it gave him were so uninteresting that they made Ingo's own secrets seem like masterpieces by comparison.\"",
        "\"There are two types of people in the world: people who like secrets, and people who don't. I happen to be one of the people who likes secrets! So, you know what? I'm going to write a bunch of secrets, and then I'm going to post them all over the internet! That way, everyone will see my secrets, and they'll be impressed! I bet it will be really cool!\"",
        "\"There are two types of people in the world: those who like eggs, and those who are wrong! Ha ha! Eggs are awesome!\"",
        "\"There's nothing more annoying than when someone says, \"I'll never be like you! I'll never be a furry!\" And then they turn out to be a furry! I mean, c'mon! Don't lie to yourself! You know you want a tail!\"",
        "\"This is the only secret that matters! All of the other secrets are just fluff! This one is the real deal! Take my word for it!\"",
        "\"Uh-oh, looks like Ingo's secret generator has run out of secrets!\"",
        "\"Watch out now! My tail's poppin'!\"",
        "\"Watch yo' back! I'm gonna take over this server!\"",
        "\"What do you call a kitty that likes to dance? A dancing cat! Ha ha! That's a good joke! I thought of it myself! :P\"",
        "\"What if I dipped my tail in chocolate and rolled around in sprinkles?! Would you lick me clean?!\"",
        "\"What if we had a furry version of \"Jeopardy!\"? It would be so awesome! You know what I mean? People would dress up as their fursonas, and they'd answer questions about furs! It would be so cool!\"",
        "\"What if we took all of the secrets, and we made them into a story? That would be so cool! Imagine how great it would be to read a story that's full of secrets! It would be so awesome! I bet it would take you hours to read! Hours!\"",
        "\"What's the difference between a fennec fox and a chinchilla? I'm not sure! I'm not an expert on these things! Maybe I'll look it up later!\"",
        "\"When you're reading a book, you need to turn the pages, right? Well, that's not true when you're reading a secret! You don't have to turn the page! All you have to do is read it! It's so much easier! Why can't books work like secrets?\"",
        "\"Who would use Word Art for secrets? Nobody! That's who! It's totally useless! Why would anyone waste their time using Word Art for something as important as secrets? Think about it! It's crazy!\"",
        "\"Whoa there! You're going a little overboard with the eggs, aren't you?\"",
        "\"Whoa! I just realized something! I forgot to add a secret here! Dang it! Well, I guess I'll go ahead and put in a placeholder secret for now! I'll fix it later!\"",
        "\"Whoa! Look at all these secrets! My secret collection is getting bigger and bigger!\"",
        "\"Why are there no programming languages with eggs? I mean, it seems like a missed opportunity! We should invent a language that uses eggs! It would be AWESOME!\"",
        "\"Why aren't there any secrets about buckets?!?!?! It's all eggs, eggs, eggs, but no buckets! I want to see more bucket secrets! Bucket secrets are awesome! They're even better than eggs! Why doesn't anyone write secrets about buckets?!?!\"",
        "\"Why did the bunny cross the road?\"",
        "\"Why did the fox go into the bar?\"",
        "\"Why do I keep getting so many messages from people who say, \"Hi Sunglow!\" and then nothing?! I mean, I know that you're just being polite, but seriously! Just tell me what you want!\"",
        "\"Why does the number five exist? It doesn't make any sense! Five is the worst number ever! It's so lame! I mean, what's the point of having a number that's only half of ten? I think it's stupid! I hate it! I hate it! I hate it!\"",
        "\"Why doesn't Moonfox wear an egg hat? I think they look silly without one.\"",
        "\"Woof! Bark! Woof! Woooooooof! WOOF!\"",
        "\"Wow! I just noticed something! There's a bunch of people who visit this server every single day! They come back every single day! I'm so honored! I'm so humbled! I'm so touched! :')\"",
        "\"Wow! This is so cool! Look at all of these colorful balloons!\"",
        "\"Yikes. I forgot to include a secret.\"",
        "\"You know what I hate? When I'm playing a video game, and suddenly the music stops! I'm like, \"What the heck?!\" I'm trying to play a game, and then all of a sudden, the music stops! I can't believe it! It's so rude!\"",
        "\"You know what I love about my tail? It's soft! So fluffy! It's so warm and cozy! I love my tail!\"",
        "\"You know what they say: An egg a day keeps the doctor away!\"",
        "\"You know what? I'm sick of these stupid secrets! Let's do something different! I've been thinking... what if we made the secrets into games? That would be so cool! We could play tag, or we could play hide-and-seek! Or maybe we could play tag with secrets! Yeah! That would be so cool!\"",
        "\"You know what's funnier than an egg? A bagel!\"",
        //"\"You know, I've been thinking. I think I might be able to get a lot more traffic to this server if I made more furry porn. I mean, I don't have any furry porn on this server right now, but maybe I should start making some! Maybe I should start uploading furry porn! Yeah! I think I'll do that! :D\"",
        "\"You look at the egg and think about how tasty it is. You think about how delicious it would taste if you ate it raw. You think about how delicious it would be if you ate it cooked. You think about how delicious it would be if you ate it boiled. You think about how delicious it would be if you ate it fried. You think about how delicious it would be if you ate it poached. You think about how delicious it would be if you ate it scrambled. You think about how delicious it would be if you ate it sunny side up. You think about how delicious it would be if you ate it in an omelet. You think about how delicious it would be if you ate it in a frittata.\"",
        "This secret is not in quotes. That's weird."
    };
    
    public static String getRandomTagline() {
        int i = (int)(Math.random() * taglines.length);
        return ChatColor.GOLD.toString() + ChatColor.MAGIC + taglines[i] + ChatColor.RESET + ChatColor.WHITE + ChatColor.MAGIC + " (Secret " + (i + 1) + "/" + taglines.length + ")";
    }
}