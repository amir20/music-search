function thisMovie(movieName) {
    if (navigator.appName.indexOf("Microsoft") != -1) {
        return window[movieName];
    } else {
        return document[movieName];
    }
}

function playSong(song) {
    thisMovie("player").playSong(song); 
}

function addSongToPlaylist(song) {
    thisMovie("player").addSongToPlaylist(song);
}