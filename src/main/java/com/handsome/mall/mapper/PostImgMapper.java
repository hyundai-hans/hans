package com.handsome.mall.mapper;

import com.handsome.mall.dto.ImgDto;
import com.handsome.mall.entity.primary.Post;
import com.handsome.mall.entity.primary.PostImg;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostImgMapper {
    PostImgMapper INSTANCE = Mappers.getMapper(PostImgMapper.class);
    @Mapping(target = "post", ignore = true)
    @Mapping(source = "imgUrl", target = "imgUrl")
    @Mapping(source = "isThumbnail", target = "isThumbnail")
    @Mapping(source = "post", target = "post")
    PostImg mapToPostImg(String imgUrl, boolean isThumbnail, Post post);

    @Named("mapImgUrlsToPostImages")
    default List<PostImg> mapImgUrlsToPostImages(List<String> imgUrls) {
        List<PostImg> postImages = new ArrayList<>();
        for (int i = 0; i < imgUrls.size(); i++) {
            String url = imgUrls.get(i);
            boolean isThumbnail = (i == 0); // First image is the thumbnail
            postImages.add(mapToPostImg(url, isThumbnail));
        }
        return postImages;
    }

      @Mapping(source = "imgId", target = "id")
    PostImg imgDtoToPostImg(ImgDto imgDto);

    @Mapping(source = "id", target = "imgId")
    ImgDto postImgToImgDto(PostImg postImg);
}
