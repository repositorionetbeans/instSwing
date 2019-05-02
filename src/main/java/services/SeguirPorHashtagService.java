/*
 * Copyright 2019 Windows 10.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package services;

import java.util.List;
import java.util.Random;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.InstagramLogin;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramTagFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

/**
 *
 * @author Windows 10
 */
public class SeguirPorHashtagService {

    Instagram4j instagram = InstagramLogin.getInstagram();

    public void seguirPorHashtag(String hashtag) {

        String nextMaxId = null;
        Random aleatorio = new Random();
        int tempoPausa = 0;

        while (true) {

            try {

                InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramTagFeedRequest(hashtag, nextMaxId));
                for (InstagramFeedItem feedResult : tagFeed.getItems()) {
                    int pausaParaCada10Usuarios = aleatorio.nextInt(10000) + 9000;
                    if (tempoPausa == 10) {
                        Thread.sleep(pausaParaCada10Usuarios);
                        tempoPausa = 0;
                    }

                    int pausaParaCadaSeguida = aleatorio.nextInt(3000) + 1000;
                    Thread.sleep(pausaParaCadaSeguida);
                    instagram.sendRequest(new InstagramFollowRequest(feedResult.getUser().pk));

                    tempoPausa++;
                }

                nextMaxId = tagFeed.getNext_max_id();

                if (nextMaxId == null) {
                    break;
                }

            } catch (Exception ex) {
                System.out.println("Erro: " + ex);
            }
        }
    }
}
