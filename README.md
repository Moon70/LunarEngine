# LunarEngine

Short :

This is a very primitive graphics engine, using a simple integer array as chunky-buffer. Setting a pixel on screen means writing it´s RGB value at the right position in this array.

This is neither best practice nor state of the art. It was created just for fun (and in fact it was fun creating it), just for personal reasons, to play around, and **i never ever intended to make this public** :-).

However, my contribution to the [Revision 2020](https://2020.revision-party.net) Demoscene event is based on this engine, and because i decided to make the demo open source, the engine had to become open source, too.



Long: 

When learning Java in 2006, i recreated a game i did in 1989 using AmigaBasic. Using the Java Swing framework, the code was full of `graphics.drawImage`calls.

It was written for Java1.4, and worked as expected. Then came Java1.5, and still everything was fine.

Then came Java1.6, and BAMM, total flicker chaos. No doubt, there must be a bug in the game, maybe i´m using some API in a wrong way. Carefully reading the release notes of both Java1.5 and Java1.6 didn´t help.

Instead of wasting too much time tracing the bug, soon an idea was born: Why not replace all those `drawImage` calls with just one,- drawing only ONE fullscreen image, and using this image (it´s integer array) as chunky buffer.

Here is the result.

Later i recreated another game i did in 1992 on Amiga using assembly, and it worked just fine. That was in 2017.

And finally, in summer 2019, i discovered [Coda](https://www.pouet.net/prod.php?which=80998 "Pouet"), the **Revision 2019** contribution by my Abyss fellows, and i asked myself: Am i crazy enough to try to make a demo for **Revision 2020** based on this engine? Yes i am!

