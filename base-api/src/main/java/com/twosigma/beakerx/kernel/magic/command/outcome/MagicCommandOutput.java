/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beakerx.kernel.magic.command.outcome;

import com.twosigma.beakerx.TryResult;
import com.twosigma.beakerx.jvm.object.EvaluationObject;
import com.twosigma.beakerx.kernel.KernelFunctionality;
import com.twosigma.beakerx.kernel.MessageCreatorService;
import com.twosigma.beakerx.message.Message;
import com.twosigma.beakerx.mimetype.MIMEContainer;

import java.util.Collections;
import java.util.Optional;

import static com.twosigma.beakerx.util.Preconditions.checkNotNull;

public class MagicCommandOutput implements MagicCommandOutcomeItem {

  private Optional<MIMEContainer> mineContainer;
  private MagicCommandOutput.Status status;

  private TryResult result;
  private EvaluationObject seo;
  private MessageCreatorService messageCreatorService;

  private MagicCommandOutput(MagicCommandOutput.Status status, Optional<MIMEContainer> mineContainer, TryResult result, EvaluationObject seo, MessageCreatorService messageCreatorService) {
    this.mineContainer = mineContainer;
    this.status = checkNotNull(status);
    this.result = result;
    this.seo = seo;
    this.messageCreatorService = messageCreatorService;
  }

  public MagicCommandOutput(Status status, MessageCreatorService messageCreatorService) {
    this(status, Optional.empty(), null, null, messageCreatorService);
  }

  public MagicCommandOutput(MagicCommandOutput.Status status, String text, MessageCreatorService messageCreatorService) {
    this(status, Optional.of(MIMEContainer.Text(checkNotNull(text).concat("\n"))), null, null, messageCreatorService);
  }

  public MagicCommandOutput(Status status, String text, TryResult result, EvaluationObject seo, MessageCreatorService messageCreatorService) {
    this(status, Optional.of(MIMEContainer.Text(checkNotNull(text).concat("\n"))), result, seo, messageCreatorService);
  }

  @Override
  public Optional<MIMEContainer> getMIMEContainer() {
    return mineContainer;
  }

  public MagicCommandOutput.Status getStatus() {
    return status;
  }

  @Override
  public TryResult getResult() {
    return result;
  }

  @Override
  public EvaluationObject getSimpleEvaluationObject() {
    return seo;
  }

  @Override
  public void sendRepliesWithStatus(KernelFunctionality kernel, Message message, int executionCount) {
    if (getStatus().equals(MagicCommandOutcomeItem.Status.OK)) {
      if (getMIMEContainer().isPresent()) {
        kernel.publish(Collections.singletonList(messageCreatorService.buildOutputMessage(message, (String) getMIMEContainer().get().getData(), false)));
      }
      kernel.send(messageCreatorService.buildReplyWithOkStatus(message, executionCount));
    } else {
      kernel.publish(Collections.singletonList(messageCreatorService.buildOutputMessage(message, (String) getMIMEContainer().get().getData(), true)));
      kernel.send(messageCreatorService.buildReplyWithErrorStatus(message, executionCount));
    }
  }

  @Override
  public void sendMagicCommandOutcome(KernelFunctionality kernel, Message message, int executionCount) {
    if (getMIMEContainer().isPresent()) {
      boolean hasError = getStatus().equals(MagicCommandOutcomeItem.Status.ERROR);
      kernel.publish(Collections.singletonList(messageCreatorService.buildOutputMessage(message, (String) getMIMEContainer().get().getData(), hasError)));
    }
  }
}
