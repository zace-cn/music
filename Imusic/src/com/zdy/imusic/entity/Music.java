package com.zdy.imusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Music implements Parcelable {
	private MusicInfo[] song;

	public MusicInfo[] getSong() {
		return song;
	}

	public void setSong(MusicInfo[] song) {
		this.song = song;
	}

	public static class MusicInfo implements Parcelable {

		private String songid;
		private String songname;
		private String encrypted_songid;
		private String has_mv;
		private String yyr_artist;
		private String artistname;
		private String control;

		public String getSongid() {
			return songid;
		}

		public void setSongid(String songid) {
			this.songid = songid;
		}

		public String getSongname() {
			return songname;
		}

		public void setSongname(String songname) {
			this.songname = songname;
		}

		public String getEncrypted_songid() {
			return encrypted_songid;
		}

		public void setEncrypted_songid(String encrypted_songid) {
			this.encrypted_songid = encrypted_songid;
		}

		public String getHas_mv() {
			return has_mv;
		}

		public void setHas_mv(String has_mv) {
			this.has_mv = has_mv;
		}

		public String getYyr_artist() {
			return yyr_artist;
		}

		public void setYyr_artist(String yyr_artist) {
			this.yyr_artist = yyr_artist;
		}

		public String getArtistname() {
			return artistname;
		}

		public void setArtistname(String artistname) {
			this.artistname = artistname;
		}

		public String getControl() {
			return control;
		}

		public void setControl(String control) {
			this.control = control;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(songid);
			dest.writeString(songname);
			dest.writeString(encrypted_songid);
			dest.writeString(has_mv);
			dest.writeString(yyr_artist);
			dest.writeString(artistname);
			dest.writeString(control);
		}

		public MusicInfo() {

		}

		private MusicInfo(Parcel in) {
			songid = in.readString();
			songname = in.readString();
			encrypted_songid = in.readString();
			has_mv = in.readString();
			yyr_artist = in.readString();
			artistname = in.readString();
			control = in.readString();
		}

		public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {
			public MusicInfo createFromParcel(Parcel source) {
				return new MusicInfo(source);
			}

			public MusicInfo[] newArray(int size) {
				return new MusicInfo[size];
			}
		};
	}

	@Override
	public int describeContents() {
		return 0;
	}
 
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelableArray(song, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public Music() {
	}

	private Music(Parcel in) {
		
//		Parcelable[] phones = source
//		        .readParcelableArray(TxrjPhone.class.getClassLoader());
//		for (int i = 0; i < phones.length; i++) {
//		    contact.phoneList.add((TxrjPhone)phones[i]);
//		} 
		
		Parcelable[] songs = in.readParcelableArray(Music.class
				.getClassLoader());
		
		for(int i = 0; i < songs.length; i++){
			song[i] = (MusicInfo) songs[i];
		}
		
//		song = (MusicInfo[]) in.readParcelableArray(Music.class
//				.getClassLoader());
	}

	public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
		public Music createFromParcel(Parcel source) {
			return new Music(source);
		}

		public Music[] newArray(int size) {
			return new Music[size];
		}
	};
}
