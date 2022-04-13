package com.example.NoteTaker

import kotlin.random.Random

// This File was copied from original
class LoremIpsum {
    companion object {

        val WORDS: Array<String>

        init {
            // text pasted straight from Lorem Ipsum website
            WORDS = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent scelerisque
            tortor in tellus elementum dictum. Vivamus dignissim rutrum nibh pellentesque
            vehicula. Phasellus pretium ut lacus ut interdum. Nam sit amet pulvinar lacus.
            Pellentesque a ullamcorper nibh. Nulla iaculis nisl et ultrices auctor. Morbi
            tellus eros, ullamcorper ut neque vel, pellentesque vulputate justo. Sed vel
            vulputate nisi. Praesent tristique interdum purus in tincidunt.
            
            Cras mi orci, lacinia sit amet dui eget, vestibulum finibus libero. Vestibulum
            ultrices facilisis leo ut mollis. Quisque feugiat nisi tellus, at venenatis mi
            placerat in. In maximus velit a tempor molestie. Nulla nec ultricies mauris,
            quis dignissim nisl. Donec nibh ex, iaculis et dapibus et, pellentesque sit amet
            diam. Duis nibh augue, ultrices quis augue ac, luctus tristique lectus. Nulla
            dapibus congue malesuada.
            
            Aenean in tincidunt mi. Nulla at euismod mauris. Nunc ipsum velit, elementum id
            sem et, vestibulum aliquam nibh. Interdum et malesuada fames ac ante ipsum
            primis in faucibus. Aliquam ullamcorper enim quis eros vulputate lobortis. Sed
            sagittis felis ornare mauris commodo sollicitudin. Donec sit amet accumsan
            turpis, a feugiat nisl. In quis porttitor ipsum. Phasellus consectetur nec neque
            vel convallis. Fusce tempus congue dui nec aliquam. Nullam quis magna ut lacus
            eleifend rhoncus. Phasellus id enim pretium, auctor lectus vel, varius turpis.
            Vivamus justo arcu, ornare in dui id, finibus dapibus eros. Orci varius natoque
            penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut molestie
            facilisis auctor.
            
            Donec sit amet risus tempor turpis malesuada viverra sed in lorem. Phasellus
            elementum a nulla ut rutrum. Morbi nibh mi, elementum sed egestas non, elementum
            vulputate odio. Class aptent taciti sociosqu ad litora torquent per conubia
            nostra, per inceptos himenaeos. Aliquam ut sem ac quam sodales pretium in eu
            dui. Vivamus a mattis odio. Suspendisse iaculis orci in nisl aliquam faucibus.
            Proin tortor odio, imperdiet vel diam in, scelerisque ullamcorper nulla.
            Vestibulum aliquam dui vitae vulputate venenatis. Donec et aliquet urna, non
            vestibulum est. Pellentesque habitant morbi tristique senectus et netus et
            malesuada fames ac turpis egestas. Aenean condimentum nunc dui, eu aliquet nisl
            tincidunt at.
            
            Mauris eu sodales nisi. Integer neque turpis, laoreet sit amet nisi vitae,
            dignissim facilisis lectus. Fusce ut rutrum ligula. Vivamus vehicula a nisi a
            pharetra. Suspendisse vitae nisl non lectus gravida consectetur. Nullam at massa
            ac lectus mattis finibus. Nulla at leo a urna feugiat hendrerit a at purus.
            Curabitur purus odio, faucibus eget ligula id, accumsan fermentum felis. Aenean
            nunc massa, tincidunt et enim sit amet, tempor tempor lacus. Interdum et
            malesuada fames ac ante ipsum primis in faucibus. Sed aliquam facilisis nibh nec
            finibus. Sed id orci non arcu maximus suscipit in sit amet enim. Praesent eros
            dui, dictum et dignissim in, volutpat ut neque. Aliquam elementum consequat
            nibh, sed ornare erat dignissim eu. Pellentesque lobortis efficitur euismod. Ut
            vestibulum, erat et ornare lobortis, velit tellus euismod magna, vel mollis
            magna risus vitae est.
"""
                // do some clean up (I could have done this all in VS Code or something too,
                // doing all the regex work just seemed easier and it's only called once each
                // run since thjs is a companion object (i.e. static)
                .trim() // strip out whitespace in from of each line
                .lowercase() // all lower case text
                .replace("\\p{Punct}".toRegex(), "") // strip out punctuation
                .replace("\n", " ") // remove newlines
                .replace("\\s+".toRegex(), " ") // only 1 space between words
                .split(" ")// make a list, then an array
                .toTypedArray()

            val min = WORDS.minOf { it.length }
            val max = WORDS.maxOf { it.length }
//            for (w in WORDS) println("'$w'")
        }

        /**
         * returns sequence of num words
         */
        fun getRandomSequence(num: Int): String {
            val i = Random.nextInt(0, WORDS.size - num)
            var s = WORDS.copyOfRange(i, i + num).joinToString(" ")
            return s
        }
    }
}