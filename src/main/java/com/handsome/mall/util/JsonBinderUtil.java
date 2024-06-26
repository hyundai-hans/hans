package com.handsome.mall.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the Util class for returning the HttpServletResponse with json type
 */
public class JsonBinderUtil {

  private JsonBinderUtil(){


  }

  public static void setResponseWithJson(HttpServletResponse response, int status,
      Object type) throws IOException {
    response.setContentType("application/json");
    response.setStatus(status);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(getJsonFromType(type));
  }



  private static String getJsonFromType(Object type) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(type);
  }

}
