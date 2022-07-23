package com.example.demo.src.search;

//import com.example.demo.src.search.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.src.brand.model.GetBrandListRes;
import com.example.demo.src.search.SearchService;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    private final JwtService jwtService;

    @Autowired
    public SearchProvider(SearchDao searchDao, JwtService jwtService) {
        this.searchDao = searchDao;
        this.jwtService = jwtService;
    }

    public List<String> getSearchWord(int userIdx, String type) throws BaseException {
        try {
            List<String> getBrandListRes = searchDao.getSearchWord(userIdx,type);
            return getBrandListRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
