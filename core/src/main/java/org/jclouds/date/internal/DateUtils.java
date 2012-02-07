/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.date.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Adrian Cole
 */
public class DateUtils {

   public static final Pattern MILLIS_PATTERN = Pattern.compile("(.*\\.[0-9][0-9][0-9])[0-9]*");

   // This regexp will match all TZ forms that are valid is ISO 8601
   public static final Pattern TZ_PATTERN = Pattern.compile("(.*)([+-][0-9][0-9](:?[0-9][0-9])?|Z)");

   public static String trimToMillis(String toParse) {
       Matcher matcher = MILLIS_PATTERN.matcher(toParse);
       if (matcher.find()) {
          toParse = matcher.group(1);
       }
      return toParse;
   }

   public static final Pattern SECOND_PATTERN = Pattern.compile(".*[0-2][0-9]:00");

   public static String trimTZ(String toParse) {
      Matcher matcher = TZ_PATTERN.matcher(toParse);
      if (matcher.find()) {
         toParse = matcher.group(1);
      }
      // TODO explain why this check is here
      if (toParse.length() == 25 && SECOND_PATTERN.matcher(toParse).matches())
         toParse = toParse.substring(0, toParse.length() - 6);
      return toParse;
   }

   public static String findTZ(String toParse) {
      Matcher matcher = TZ_PATTERN.matcher(toParse);
      if (matcher.find()) {
         // Remove ':' from the TZ string, as SimpleDateFormat can't handle it
         String tz = matcher.group(2).replace(":", "");
         // Append '00; if we only have a two digit TZ, as SimpleDateFormat
         if (tz.length() == 2) tz += "00";
         return tz;
      } else {
         return "";
      }
   }

}