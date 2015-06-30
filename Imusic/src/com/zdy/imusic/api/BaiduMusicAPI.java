package com.zdy.imusic.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.zdy.imusic.entity.Lyrics;
import com.zdy.imusic.entity.Music;
import com.zdy.imusic.entity.Song;

public interface BaiduMusicAPI {
	@GET("/ting")
	void getMusicInfo(@Query("from") String from,
			@Query("method") String method, @Query("format") String format,
			@Query("query") String query, Callback<Music> callback);
	
	@GET("/ting")
	void getMusicById(@Query("from") String from,
			@Query("method") String method, @Query("format") String format,
			@Query("songid") String query, Callback<Song> callback);
	//http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.play&format=json&songid=121523276
	//http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.lry&format=json&songid=121523276

	@GET("/ting")
	Song getMusicById(@Query("from") String from,
			@Query("method") String method, @Query("format") String format,
			@Query("songid") String query);

	
	@GET("/ting")
	Lyrics getMusicLycById(@Query("from") String from,
			@Query("method") String method, @Query("format") String format,
			@Query("songid") String query);
	//http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.lry&format=json&songid=121523276
}
