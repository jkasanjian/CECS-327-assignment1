package model;

import java.util.List;

public class Playlist {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private List<MusicClass> musicClassList;

    public List<MusicClass> getMusicClassList() {
        return musicClassList;
    }

    public void setMusicClassList(List<MusicClass> musicClassList) {
        this.musicClassList = musicClassList;
    }

    public Playlist(String name, List<MusicClass> musicClassList) {
        this.musicClassList = musicClassList;
        this.name = name;
    }

    public void addSong(MusicClass selectedSong) {
        musicClassList.add(selectedSong);
    }
}
