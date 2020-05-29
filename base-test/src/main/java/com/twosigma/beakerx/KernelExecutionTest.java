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
package com.twosigma.beakerx;

import com.twosigma.beakerx.evaluator.EvaluationObjectFactory;
import com.twosigma.beakerx.evaluator.EvaluatorResultTestWatcher;
import com.twosigma.beakerx.kernel.Code;
import com.twosigma.beakerx.kernel.KernelRunner;
import com.twosigma.beakerx.kernel.MessageCreatorService;
import com.twosigma.beakerx.kernel.comm.Comm;
import com.twosigma.beakerx.kernel.magic.command.CodeFactory;
import com.twosigma.beakerx.kernel.magic.command.functionality.AddImportMagicCommandInfo;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathAddJarMagicCommandInfo;
import com.twosigma.beakerx.kernel.magic.command.functionality.LoadMagicMagicCommandInfo;
import com.twosigma.beakerx.kernel.magic.command.functionality.UnImportMagicCommandInfo;
import com.twosigma.beakerx.message.Message;
import com.twosigma.beakerx.widget.TestWidgetUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.twosigma.MessageAssertions.verifyExecuteReplyMessage;
import static com.twosigma.beakerx.MessageFactorTest.commMsg;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class KernelExecutionTest extends ClasspathManagerTest {

  public static final String DEMO_RESOURCES_JAR = "./src/test/resources";
  public static final String DEMO_JAR_NAME = "demo.jar";
  public static final String DEMO_JAR = DEMO_RESOURCES_JAR + "/" + DEMO_JAR_NAME;
  public static final String LOAD_MAGIC_JAR_DEMO_JAR_NAME = "loadMagicJarDemo.jar";
  public static final String LOAD_MAGIC_DEMO_JAR = DEMO_RESOURCES_JAR + "/" + LOAD_MAGIC_JAR_DEMO_JAR_NAME;
  private MessageCreatorService messageCreatorService;
  private EvaluationObjectFactory evaluationObjectFactory;

  public KernelExecutionTest(KernelRunner kernelRunner, MessageCreatorService messageCreatorService, EvaluationObjectFactory evaluationObjectFactory) {
    super(kernelRunner);
    this.messageCreatorService = messageCreatorService;
    this.evaluationObjectFactory = evaluationObjectFactory;
  }

  private CodeFactory getCodeFactory() {
    return new CodeFactory(messageCreatorService, evaluationObjectFactory);
  }

  @Test
  public void evaluate16Divide2() throws Exception {
    //given
    String code = codeFor16Divide2();
    Message message = MessageFactoryTest.getExecuteRequestMessage(code);
    //when
    getKernelSocketsService().handleMsg(message);
    //then
    Optional<Message> idleMessage = EvaluatorResultTestWatcher.waitForIdleMessage(getKernelSocketsService().getKernelSockets());
    assertThat(idleMessage).isPresent();
    Optional<Message> result = EvaluatorResultTestWatcher.waitForResult(getKernelSocketsService().getKernelSockets());
    checkResultForErrors(result, code);
    assertThat(result).isPresent();
    verifyResult(result.get());
    verifyPublishedMsgs(getKernelSocketsService());
    EvaluatorResultTestWatcher.waitForSentMessage(getKernelSocketsService().getKernelSockets());
    verifySentMsgs(getKernelSocketsService());
  }

  protected void checkResultForErrors(Optional<Message> result, String code) throws InterruptedException {
    if (!result.isPresent()) {
      Optional<Message> error = EvaluatorResultTestWatcher.waitForErrorMessage(getKernelSocketsService().getKernelSockets());
      String errorMsg;
      if (error.isPresent()) {
        errorMsg = "Error message received instead of result:\n"
                + "Code: " + code + "\n"
                + error.get().getContent().toString() + "\n";
      } else {
        errorMsg = "Result nor error messages found:\n" +
                String.join(",",
                        getKernelSocketsService().getPublishedMessages().stream()
                                .map(m -> m.getHeader().getType())
                                .collect(Collectors.toList())) + "\n";
      }
      throw new AssertionError(errorMsg);
    }
  }

  protected String codeFor16Divide2() {
    return "16/2";
  }

  private void verifyPublishedMsgs(KernelSocketsServiceTest service) {
    assertThat(service.getBusyMessage()).isPresent();
    assertThat(service.getExecuteInputMessage()).isPresent();
    assertThat(service.getExecuteResultMessage()).isPresent();
    assertThat(service.getIdleMessage()).isPresent();
  }

  private void verifySentMsgs(KernelSocketsServiceTest service) {
    verifyExecuteReplyMessage(service.getReplyMessage());
  }

  private void verifyResult(Message result) {
    Map actual = ((Map) result.getContent().get(Comm.DATA));
    String value = (String) actual.get("text/plain");
    assertThat(value).isEqualTo("8");
  }

  @Test
  public void loadMagicCommand() throws Exception {
    //given
    addJarWithCustomMagicCommand();
    //when
    loadMagicCommandByClass();
    //then
    verifyLoadedMagicCommand();
  }

  protected void verifyLoadedMagicCommand() throws InterruptedException {
    String allCode = "%showEnvs";
    Code code = getCodeFactory().create(allCode, commMsg(), getKernel());
    code.execute(getKernel(), 3);
    List<Message> std = EvaluatorResultTestWatcher.waitForStdouts(getKernelSocketsService().getKernelSockets());
    String text = (String) std.get(1).getContent().get("text");
    assertThat(text).contains("PATH");
  }

  protected void loadMagicCommandByClass() throws InterruptedException {
    String allCode = LoadMagicMagicCommandInfo.LOAD_MAGIC + "   com.twosigma.beakerx.custom.magic.command.ShowEnvsCustomMagicCommand";
    Code code = getCodeFactory().create(allCode, commMsg(), getKernel());
    code.execute(getKernel(), 2);
    List<Message> std = EvaluatorResultTestWatcher.waitForStdouts(getKernelSocketsService().getKernelSockets());
    String text = (String) std.get(0).getContent().get("text");
    assertThat(text).contains("Magic command %showEnvs was successfully added.");
  }

  protected void addJarWithCustomMagicCommand() throws InterruptedException {
    String allCode = ClasspathAddJarMagicCommandInfo.CLASSPATH_ADD_JAR + " " + LOAD_MAGIC_DEMO_JAR;
    Code code = getCodeFactory().create(allCode, commMsg(), getKernel());
    code.execute(getKernel(), 1);
    Optional<Message> updateMessage = EvaluatorResultTestWatcher.waitForUpdateMessage(getKernelSocketsService().getKernelSockets());
    String text = (String) TestWidgetUtils.getState(updateMessage.get()).get("value");
    assertThat(text).contains("loadMagicJarDemo.jar");
  }

  @Test
  public void shouldImportFromAddedDemoJar() throws Exception {
    //given
    //when
    addDemoJar();
    //then
    verifyAddedDemoJar();
  }

  private void verifyAddedDemoJar() throws InterruptedException {
    String code = codeForVerifyingAddedDemoJar();
    Message message = MessageFactoryTest.getExecuteRequestMessage(code);
    //when
    getKernelSocketsService().handleMsg(message);
    //then
    Optional<Message> idleMessage = EvaluatorResultTestWatcher.waitForIdleMessage(getKernelSocketsService().getKernelSockets());
    assertThat(idleMessage).isPresent();
    Optional<Message> result = EvaluatorResultTestWatcher.waitForResult(getKernelSocketsService().getKernelSockets());
    checkResultForErrors(result, code);
    verifyResultOfAddedJar(result.get());
  }

  protected String codeForVerifyingAddedDemoJar() {
    return "import com.example.Demo\n" +
            "new Demo().getObjectTest()";
  }

  private void verifyResultOfAddedJar(Message message) {
    Map actual = ((Map) message.getContent().get(Comm.DATA));
    String value = (String) actual.get("text/plain");
    assertThat(value).contains("Demo_test_123");
  }

  protected void addDemoJar() throws InterruptedException {
    String allCode = ClasspathAddJarMagicCommandInfo.CLASSPATH_ADD_JAR + " " + DEMO_JAR;
    Code code = getCodeFactory().create(allCode, commMsg(), getKernel());
    code.execute(getKernel(), 1);
    Optional<Message> updateMessage = EvaluatorResultTestWatcher.waitForUpdateMessage(getKernelSocketsService().getKernelSockets());
    String text = (String) TestWidgetUtils.getState(updateMessage.get()).get("value");
    assertThat(text).contains("demo.jar");
  }

  @Test
  public void shouldImportDemoClassByMagicCommand() throws Exception {
    //given
    addDemoJar();
    String path = pathToDemoClassFromAddedDemoJar();
    //when
    Code code = getCodeFactory().create(AddImportMagicCommandInfo.IMPORT + " " + path, commMsg(), getKernel());
    code.execute(kernel, 1);
    //then
    verifyImportedDemoClassByMagicCommand();
  }

  private void verifyImportedDemoClassByMagicCommand() throws InterruptedException {
    String allCode = getObjectTestMethodFromAddedDemoJar();
    Message message = MessageFactoryTest.getExecuteRequestMessage(allCode);
    getKernelSocketsService().handleMsg(message);
    Optional<Message> idleMessage = EvaluatorResultTestWatcher.waitForIdleMessage(getKernelSocketsService().getKernelSockets());
    assertThat(idleMessage).isPresent();
    Optional<Message> result = EvaluatorResultTestWatcher.waitForResult(getKernelSocketsService().getKernelSockets());
    checkResultForErrors(result, allCode);
    assertThat(result).isPresent();
    Map actual = ((Map) result.get().getContent().get(Comm.DATA));
    String value = (String) actual.get("text/plain");
    assertThat(value).contains("Demo_test_123");
  }

  protected String pathToDemoClassFromAddedDemoJar() {
    return "com.example.Demo";
  }

  protected String getObjectTestMethodFromAddedDemoJar() {
    return "new Demo().getObjectTest()";
  }

  @Test
  public void shouldImportDemoClassWithWildcardByMagicCommand() throws Exception {
    //given
    addDemoJar();
    String path = pathToDemoClassFromAddedDemoJar();
    String allCode = AddImportMagicCommandInfo.IMPORT + " " + path.substring(0, path.lastIndexOf(".")) + ".*";
    //when
    Code code = getCodeFactory().create(allCode, commMsg(), getKernel());
    code.execute(kernel, 1);
    //then
    verifyImportedDemoClassByMagicCommand();
  }

  @Test
  public void shouldNotImportClassesFromUnknownPackageWithWildcardByMagicCommand() throws Exception {
    //given
    String path = pathToDemoClassFromAddedDemoJar();
    String allCode = AddImportMagicCommandInfo.IMPORT + " " + (path.substring(0, path.lastIndexOf(".")) + "Unknown.*");
    addDemoJar();
    //when
    Code code = getCodeFactory().create(allCode, commMsg(), getKernel());
    code.execute(kernel, 1);
    //then
    List<Message> std = EvaluatorResultTestWatcher.waitForStderr(getKernelSocketsService().getKernelSockets());
    String text = (String) std.get(0).getContent().get("text");
    assertThat(text).contains("Could not import");
  }

  @Test
  public void shouldNotImportUnknownClassByMagicCommand() throws Exception {
    //given
    String allCode = AddImportMagicCommandInfo.IMPORT + " " + pathToDemoClassFromAddedDemoJar() + "UnknownClass";
    //when
    Code code = getCodeFactory().create(allCode, commMsg(), getKernel());
    code.execute(kernel, 1);
    //then
    List<Message> std = EvaluatorResultTestWatcher.waitForStderr(getKernelSocketsService().getKernelSockets());
    String text = (String) std.get(0).getContent().get("text");
    assertThat(text).contains("Could not import");
  }

  @Test
  public void shouldUnimportDemoClassByMagicCommand() throws Exception {
    //given
    addDemoJar();
    String path = pathToDemoClassFromAddedDemoJar();
    Code code = getCodeFactory().create(AddImportMagicCommandInfo.IMPORT + " " + path, commMsg(), getKernel());
    code.execute(kernel, 1);
    //when
    Code code2 = getCodeFactory().create(UnImportMagicCommandInfo.UNIMPORT + " " + path, commMsg(), getKernel());
    code2.execute(kernel, 2);
    //then
    //assertThat(status).isEqualTo(MagicCommandOutcomeItem.Status.OK);
    verifyUnImportedDemoClassByMagicCommand();
  }

  protected void verifyUnImportedDemoClassByMagicCommand() throws InterruptedException {
    String allCode = getObjectTestMethodFromAddedDemoJar();
    Message message = MessageFactoryTest.getExecuteRequestMessage(allCode);
    getKernelSocketsService().handleMsg(message);
    Optional<Message> idleMessage = EvaluatorResultTestWatcher.waitForIdleMessage(getKernelSocketsService().getKernelSockets());
    assertThat(idleMessage).isPresent();
    Optional<Message> errorMessage = EvaluatorResultTestWatcher.waitForErrorMessage(getKernelSocketsService().getKernelSockets());
    Object actual = ((Map) errorMessage.get().getContent()).get("text");
    String value = (String) actual;
    assertThat(value).contains(unimportErrorMessage());
  }

  protected String unimportErrorMessage() {
    return "unable";
  }

}
