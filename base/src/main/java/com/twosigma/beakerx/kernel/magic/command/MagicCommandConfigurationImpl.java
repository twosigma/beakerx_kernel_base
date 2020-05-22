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
package com.twosigma.beakerx.kernel.magic.command;

import com.twosigma.beakerx.autocomplete.MagicCommandAutocompletePatterns;
import com.twosigma.beakerx.evaluator.SimpleEvaluationObjectFactory;
import com.twosigma.beakerx.kernel.KernelFunctionality;
import com.twosigma.beakerx.kernel.magic.command.functionality.AddImportMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.AddImportMagicCommandInfo;
import com.twosigma.beakerx.kernel.magic.command.functionality.AddStaticImportMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.AsyncMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.BashMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClassPathAddMvnCellMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathAddDynamicMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathAddJarMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathAddJarMagicCommandInfo;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathAddMvnMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathAddRepoMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathResetMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.ClasspathShowMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.FileService;
import com.twosigma.beakerx.kernel.magic.command.functionality.HtmlAliasMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.HtmlMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.JSMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.JavaScriptMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.LoadMagicMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.LsMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.TimeCellModeMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.TimeItCellModeMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.TimeItLineModeMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.TimeLineModeMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.UnImportMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.ClojureMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.GroovyMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.JavaMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.KernelMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.KotlinMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.PythonMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.SQLMagicCommand;
import com.twosigma.beakerx.kernel.magic.command.functionality.kernelMagic.ScalaMagicCommand;
import com.twosigma.beakerx.kernel.msg.MessageCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MagicCommandConfigurationImpl implements MagicCommandConfiguration {

  private MagicCommandAutocompletePatterns autocompletePatterns;
  private FileService fileService;
  private MavenJarResolverService resolverService;
  private SimpleEvaluationObjectFactory objectFactory;

  public MagicCommandConfigurationImpl(FileService fileService, MavenJarResolverService mavenJarResolverService, MagicCommandAutocompletePatterns patterns) {
    this.fileService = fileService;
    this.resolverService = mavenJarResolverService;
    this.autocompletePatterns = patterns;
    this.objectFactory = new SimpleEvaluationObjectFactory();
  }

  public MagicCommandAutocompletePatterns patterns() {
    return autocompletePatterns;
  }

  public FileService getFileService() {
    return fileService;
  }

  public List<MagicCommandType> createDefaults(KernelFunctionality kernel) {
    List<MagicCommandType> magicCommandTypes = new ArrayList<>();
    magicCommandTypes.addAll(
            Arrays.asList(
                    javascript(),
                    js(),
                    html(),
                    HTML(),
                    bash(),
                    lsmagic(magicCommandTypes),
                    addJar(kernel),
                    addJarByMvn(kernel),
                    addJarByMvnCell(kernel),
                    addDynamic(kernel),
                    addRepo(kernel),
                    addClasspathReset(kernel),
                    showClasspath(kernel),
                    addStaticImport(kernel),
                    addImport(kernel),
                    unimport(kernel),
                    timeLine(kernel),
                    timeCell(kernel),
                    timeItLine(kernel),
                    timeItCell(kernel),
                    loadMagic(kernel),
                    kernel(kernel),
                    //aliases for kernel magic
                    python(kernel),
                    clojure(kernel),
                    groovy(kernel),
                    java(kernel),
                    kotlin(kernel),
                    scala(kernel),
                    sql(kernel),
                    async(kernel)));
    return magicCommandTypes;
  }

  public MagicCommandType async(KernelFunctionality kernel) {
    return new MagicCommandType(AsyncMagicCommand.ASYNC, "", new AsyncMagicCommand(kernel, objectFactory));
  }

  public ClasspathAddMvnMagicCommand getClasspathAddMvnMagicCommand(KernelFunctionality kernel) {
    Optional<MagicCommandType> first = kernel.getMagicCommandTypes().stream()
            .filter(x -> x.getCommand().equals(ClasspathAddMvnMagicCommand.CLASSPATH_ADD_MVN))
            .findFirst();
    return (ClasspathAddMvnMagicCommand) first.get().getMagicCommandFunctionality();
  }

  private MagicCommandType addClasspathReset(KernelFunctionality kernel) {
    return new MagicCommandType(ClasspathResetMagicCommand.CLASSPATH_RESET, "", new ClasspathResetMagicCommand(kernel, fileService));
  }

  private MagicCommandType addDynamic(KernelFunctionality kernel) {
    return new MagicCommandType(ClasspathAddDynamicMagicCommand.CLASSPATH_ADD_DYNAMIC, "", new ClasspathAddDynamicMagicCommand(kernel, MessageCreator.get(), objectFactory));
  }

  private MagicCommandType loadMagic(KernelFunctionality kernel) {
    return new MagicCommandType(LoadMagicMagicCommand.LOAD_MAGIC, "", new LoadMagicMagicCommand(kernel, MessageCreator.get()));
  }

  private MagicCommandType timeItCell(KernelFunctionality kernel) {
    return new MagicCommandType(TimeItCellModeMagicCommand.TIMEIT_CELL, "", new TimeItCellModeMagicCommand(kernel, objectFactory));
  }

  private MagicCommandType timeItLine(KernelFunctionality kernel) {
    return new MagicCommandType(TimeItLineModeMagicCommand.TIMEIT_LINE, "", new TimeItLineModeMagicCommand(kernel, objectFactory));
  }

  private MagicCommandType timeCell(KernelFunctionality kernel) {
    return new MagicCommandType(TimeCellModeMagicCommand.TIME_CELL, "", new TimeCellModeMagicCommand(kernel, objectFactory));
  }

  private MagicCommandType timeLine(KernelFunctionality kernel) {
    return new MagicCommandType(TimeLineModeMagicCommand.TIME_LINE, "", new TimeLineModeMagicCommand(kernel, objectFactory));
  }

  private MagicCommandType unimport(KernelFunctionality kernel) {
    return new MagicCommandType(UnImportMagicCommand.UNIMPORT, "<classpath>", new UnImportMagicCommand(kernel, MessageCreator.get()));
  }

  private MagicCommandType addImport(KernelFunctionality kernel) {
    return new MagicCommandType(AddImportMagicCommandInfo.IMPORT, "<classpath>", new AddImportMagicCommand(kernel, MessageCreator.get()));
  }

  private MagicCommandType addStaticImport(KernelFunctionality kernel) {
    return new MagicCommandType(AddStaticImportMagicCommand.ADD_STATIC_IMPORT, "<classpath>", new AddStaticImportMagicCommand(kernel));
  }

  private MagicCommandType showClasspath(KernelFunctionality kernel) {
    return new MagicCommandType(ClasspathShowMagicCommand.CLASSPATH_SHOW, "", new ClasspathShowMagicCommand(kernel, MessageCreator.get()));
  }

  private MagicCommandType addJarByMvn(KernelFunctionality kernel) {
    return new MagicCommandType(ClasspathAddMvnMagicCommand.CLASSPATH_ADD_MVN, "<group name version>",
            new ClasspathAddMvnMagicCommand(resolverService.getParams(kernel), kernel, MessageCreator.get()));
  }

  private MagicCommandType addJarByMvnCell(KernelFunctionality kernel) {
    return new MagicCommandType(ClassPathAddMvnCellMagicCommand.CLASSPATH_ADD_MVN_CELL, "<group name version>",
            new ClassPathAddMvnCellMagicCommand(resolverService.getParams(kernel), kernel, MessageCreator.get()));
  }

  private MagicCommandType addJar(KernelFunctionality kernel) {
    return new MagicCommandType(ClasspathAddJarMagicCommandInfo.CLASSPATH_ADD_JAR, "<jar path>", new ClasspathAddJarMagicCommand(kernel, MessageCreator.get()));
  }

  private MagicCommandType addRepo(KernelFunctionality kernel) {
    return new MagicCommandType(ClasspathAddRepoMagicCommand.CLASSPATH_CONFIG_RESOLVER, "<repoName repoUrl>", new ClasspathAddRepoMagicCommand(kernel));
  }

  private MagicCommandType lsmagic(List<MagicCommandType> magicCommandTypes) {
    return new MagicCommandType(LsMagicCommand.LSMAGIC, "", new LsMagicCommand(magicCommandTypes));
  }

  private MagicCommandType bash() {
    return new MagicCommandType(BashMagicCommand.BASH, "", new BashMagicCommand());
  }

  private MagicCommandType html() {
    return new MagicCommandType(HtmlMagicCommand.HTML, "", new HtmlMagicCommand());
  }

  private MagicCommandType HTML() {
    return new MagicCommandType(HtmlAliasMagicCommand.HTML, "", new HtmlAliasMagicCommand());
  }

  private MagicCommandType javascript() {
    return new MagicCommandType(JavaScriptMagicCommand.JAVASCRIPT, "", new JavaScriptMagicCommand());
  }

  private MagicCommandType js() {
    return new MagicCommandType(JSMagicCommand.JAVASCRIPT, "", new JSMagicCommand());
  }

  private MagicCommandType kernel(KernelFunctionality kernel) {
    return new MagicCommandType(KernelMagicCommand.KERNEL, "", new KernelMagicCommand(kernel));
  }

  private MagicCommandType python(KernelFunctionality kernel) {
    return new MagicCommandType(PythonMagicCommand.PYTHON, "", new PythonMagicCommand(kernel));
  }

  private MagicCommandType sql(KernelFunctionality kernel) {
    return new MagicCommandType(SQLMagicCommand.SQL, "", new SQLMagicCommand(kernel));
  }

  private MagicCommandType scala(KernelFunctionality kernel) {
    return new MagicCommandType(ScalaMagicCommand.SCALA, "", new ScalaMagicCommand(kernel));
  }

  private MagicCommandType kotlin(KernelFunctionality kernel) {
    return new MagicCommandType(KotlinMagicCommand.KOTLIN, "", new KotlinMagicCommand(kernel));
  }

  private MagicCommandType java(KernelFunctionality kernel) {
    return new MagicCommandType(JavaMagicCommand.JAVA, "", new JavaMagicCommand(kernel));
  }

  private MagicCommandType groovy(KernelFunctionality kernel) {
    return new MagicCommandType(GroovyMagicCommand.GROOVY, "", new GroovyMagicCommand(kernel));
  }

  private MagicCommandType clojure(KernelFunctionality kernel) {
    return new MagicCommandType(ClojureMagicCommand.CLOJURE, "", new ClojureMagicCommand(kernel));
  }
}
