AtomDesktop
===========


What is it ?
------------

A smart desktop assistant who answer all your questions. 
Application Programming Interfaces (APIs) used in this project :
- Google Speech Recognition API (Google has not publicly covered the API, so I do not grant it will be always 100% working). Doesn't require any APPID or whatever.
- Wolfram Alpha API. You have to register on [WolframAlpha's Developer portal](https://developer.wolframalpha.com/portal/apisignup.html) and register an application. Then enter the ID given to you as value of the static String "appid" in WolframRequest.java 

This application MUST be used with a PHP server you have access to (access means that you have the username, password and ftp server adress of this server).
There are great Web Hosting services such as [alwaysdata.com](https://www.alwaysdata.com/) wich offers free mutualised hosting plans (for free) that support a bunch of languages (Django, Ruby on Rails, Symfony, Python, PHP, and others)

**What do I put on my server?**

[https://github/chlkbumper/AtomServer](https://github.com/chlkbumper/AtomServer)


How to use it
-------------

When opening Eclipse, select as workspace the "eclipse" folder you've extracted from the downloaded archive (zip, or even tarball).
If the project isn't already on the Package Explorer, create a project called `Atom`. 
The editable content is located in the package `me.atom.windowsj`.
In `WolframRequest.java`, fill the String called `appid` (line 17) with the App ID given to you by WolframAlpha.
You will also have to edit in `FTPAtomUploader.java` the Strings called `userName`, `password`, and `ftpServer`.

When you're done, go into the main class (`Atom.java`) and Run the app. If you have any issues with the software, please feedback here.


Licensing
---------

This software is free. You are allowed to reuse it under the terms of the [Creative Commons Attributions-NonCommercial-ShareAlike 3.0](http://creativecommons.org/licenses/by-nc-sa/3.0/) license. it basically means you can do whatever you want with it. Though, you are NOT allowed to sell it, or even commercially, to make profit.
If you would like to use it for more than the previous license allows (e.g. run a commercial server that makes people pay for it), please contact me and ask for a special (commercial) license.




Disclaimer
----------

    IMPORTANT:  This software is supplied to you by Guillaume Cendre ("chlkbumper") in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this software.
    
    In consideration of your agreement to abide by the following terms, and subject to these terms, chlkbumper grants you a personal, non-exclusive license, under chlkbumper's copyrights in this original software, to use, reproduce, modify and redistribute the software, with or without modifications, in source and/or binary forms; provided that if you redistribute this software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the software. Neither the name, trademarks, service marks or logos of chlkbumper may be used to endorse or promote products derived from this software without specific prior written permission from chlkbumper.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by chlkbumper herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which this software may be incorporated.
    
    This software is provided by Guillaume Cendre ("chlkbumper") on an "AS IS" basis.  CHLKBUMPER MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THIS SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
    
    IN NO EVENT SHALL GUILLAUME CENDRE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF GUILLAUME CENDRE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Copyright (C) 2013 Guillaume Cendre. All Rights Reserved.
