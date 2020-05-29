/*
 *  Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx.kernel;

import com.twosigma.beakerx.jvm.object.EvaluationObject;
import com.twosigma.beakerx.kernel.msg.MessageHolder;
import com.twosigma.beakerx.message.Message;

import java.util.List;

public interface MessageCreatorService {

  Message buildReplyWithErrorStatus(Message message, int executionCount);

  List<MessageHolder> createMessage(EvaluationObject seo);

  Message createBusyMessage(Message parentMessage);

  Message createIdleMessage(Message parentMessage);

  Message buildReplyWithOkStatus(Message message, int executionCount);

  Message buildOutputMessage(Message message, String text, boolean hasError);
}
