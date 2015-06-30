package com.zdy.imusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

	private Bitrate bitrate;
	private SongInfo songinfo;

	public Bitrate getBitrate() {
		return bitrate;
	}

	public void setBitrate(Bitrate bitrate) {
		this.bitrate = bitrate;
	}

	public SongInfo getSonginfo() {
		return songinfo;
	}

	public void setSonginfo(SongInfo songinfo) {
		this.songinfo = songinfo;
	}

	public static class SongInfo implements Parcelable {

		private String pic_big;
		private String pic_small;
		private String lrclink;
		private String pic_radio;
		private String pic_premium;
		private String pic_huge;
		private String title;
		private String author;
		private String song_id;
		
		
		public String getSong_id() {
			return song_id;
		}

		public void setSong_id(String song_id) {
			this.song_id = song_id;
		}

		public SongInfo() {
		}

		public String getPic_big() {
			return pic_big;
		}

		public void setPic_big(String pic_big) {
			this.pic_big = pic_big;
		}

		public String getPic_small() {
			return pic_small;
		}

		public void setPic_small(String pic_small) {
			this.pic_small = pic_small;
		}

		public String getLrclink() {
			return lrclink;
		}

		public void setLrclink(String lrclink) {
			this.lrclink = lrclink;
		}

		public String getPic_radio() {
			return pic_radio;
		}

		public void setPic_radio(String pic_radio) {
			this.pic_radio = pic_radio;
		}

		public String getPic_premium() {
			return pic_premium;
		}

		public void setPic_premium(String pic_premium) {
			this.pic_premium = pic_premium;
		}

		public String getPic_huge() {
			return pic_huge;
		}

		public void setPic_huge(String pic_huge) {
			this.pic_huge = pic_huge;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {

			dest.writeString(pic_big);
			dest.writeString(pic_small);
			dest.writeString(lrclink);
			dest.writeString(pic_radio);
			dest.writeString(pic_premium);
			dest.writeString(pic_huge);
			dest.writeString(title);
			dest.writeString(author);
			dest.writeString(song_id);
		}

		private SongInfo(Parcel in) {
			pic_big = in.readString();
			pic_small = in.readString();
			lrclink = in.readString();
			pic_radio = in.readString();
			pic_premium = in.readString();
			pic_huge = in.readString();
			title = in.readString();
			author = in.readString();
			song_id = in.readString();
		}

		public static final Parcelable.Creator<SongInfo> CREATOR = new Parcelable.Creator<SongInfo>() {
			public SongInfo createFromParcel(Parcel source) {
				return new SongInfo(source);
			}

			public SongInfo[] newArray(int size) {
				return new SongInfo[size];
			}
		};

	}

	public static class Bitrate implements Parcelable {
		private String file_link;
		private String show_link;
		private String file_extension;
		private String file_size;
		private int file_duration;

		public Bitrate() {
		}

		public int getFile_duration() {
			return file_duration;
		}

		public void setFile_duration(int file_duration) {
			this.file_duration = file_duration;
		}

		public String getFile_link() {
			return file_link;
		}

		public void setFile_link(String file_link) {
			this.file_link = file_link;
		}

		public String getShow_link() {
			return show_link;
		}

		public void setShow_link(String show_link) {
			this.show_link = show_link;
		}

		public String getFile_extension() {
			return file_extension;
		}

		public void setFile_extension(String file_extension) {
			this.file_extension = file_extension;
		}

		public String getFile_size() {
			return file_size;
		}

		public void setFile_size(String file_size) {
			this.file_size = file_size;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(file_link);
			dest.writeString(show_link);
			dest.writeString(file_extension);
			dest.writeString(file_size);
			dest.writeInt(file_duration);
		}

		private Bitrate(Parcel in) {
			file_link = in.readString();
			show_link = in.readString();
			file_extension = in.readString();
			file_size = in.readString();
			file_duration = in.readInt();
		}

		public static final Parcelable.Creator<Bitrate> CREATOR = new Parcelable.Creator<Bitrate>() {
			public Bitrate createFromParcel(Parcel source) {
				return new Bitrate(source);
			}

			public Bitrate[] newArray(int size) {
				return new Bitrate[size];
			}
		};

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(bitrate, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(songinfo, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public Song() {

	}

	private Song(Parcel in) {
		bitrate = in.readParcelable(Song.class.getClassLoader());
		songinfo = in.readParcelable(Song.class.getClassLoader());
	}

	public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
		public Song createFromParcel(Parcel source) {
			return new Song(source);
		}

		public Song[] newArray(int size) {
			return new Song[size];
		}
	};

}
