imgrgb
=======
![preview][preview]
Program to use all RGB colors in a single image, based heavily upon [József Fejes ideas] (http://joco.name/2014/03/02/all-rgb-colors-in-one-image/)

The base algorithm is similar to what is described by József Fejes in his blog posts and the [stackoverflow puzzle](http://codegolf.stackexchange.com/questions/22144/images-with-all-colors). This version is implemented in Java, and has a variety of possible color distance metrics. Attempts were also made to create a more visually pleasing aesthetic by modifying the pixel placement algorithm. Output and a better algorithm description to follow.

You can see additional example renders as well as movies illustrating my version of the algorithm [here] (https://github.com/skycook/resources/tree/master/imgrgb)



TODO List
====
- [ ] Finish documentation for important functions
- [ ] Main class cleanup and refactor
- [ ] Command line input
- [ ] Generic directory output (currently hardcoded for my machine...)


[preview]:https://github.com/skycook/resources/blob/master/imgrgb/imgrgb_sumSqRGBDist_hueComparator_1023x511s200-LnQd-cent.png
