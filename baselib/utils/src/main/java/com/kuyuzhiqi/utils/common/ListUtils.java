    package com.kuyuzhiqi.utils.common;

    import android.text.TextUtils;

    import java.util.ArrayList;
    import java.util.List;

    public class ListUtils {

        /**
         * 按targetSize切分List
         */
        public static <T> List<List<T>> cutListBySize(int targetSize, List<T> inputList) {
            List<List<T>> resultList = new ArrayList<>();
            if (inputList == null || inputList.isEmpty()) {
                return resultList;
            }

            int inputListSize = inputList.size();
            int start = 0;
            int end = targetSize;
            while (start < inputListSize) {
                if (end > inputListSize) {
                    end = inputListSize;
                }

                List<T> subList = inputList.subList(start, end);
                resultList.add(subList);

                start += targetSize;
                end += targetSize;
            }

            return resultList;
        }

        /**
         * 列表是否为空,列表为null时返回空
         */
        public static boolean isEmpty(List list) {
            return list == null || list.isEmpty();
        }

        public static String join(CharSequence delimiter, Iterable tokens) {
            if(tokens == null) return "";
            return TextUtils.join(delimiter, tokens);
        }
    }
