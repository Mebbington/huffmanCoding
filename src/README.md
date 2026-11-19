What kinds of file lead to lots of compressions? 
.txt files and .html 's  we found have around 40% compression which is waaaay higher than that of 
the image files' 13%. We assume this is due to the fact that in standard books, writings, and such
there are only so many character (most of which repeat) that can be coded. Whereas
in photos, the fact that there are 256 values for RGB (most of which do NOT repeat) results
in less compression.

What kind of files had little or no compression?
.tif files resulted in little or no compression as they are high definition image files that rarely 
use multiple of the same color pixel, so encoding each color using a HuffTree is not that efficient 
as the resulting amount of unique bit sequences resulting from traversing the HuffTree is not 
significantly smaller than the number of original pixels.

What happens when you try and compress a huffman code file?
Absolute glorious compression maybe happens! Since our algorithm is so superb, compressing a .hf
does NOT result in an error. Although, after getting the difference of the original .hf and the 
.hf.hf file, we noticed the byte difference was NOT helpful. Actually, it was devious, and took up 
MORE SPACE!!! After double uncompressing this new file we get the exact same bytes and message as 
the original file. Chocolate Fluffy Cake and CHA-CHING!
