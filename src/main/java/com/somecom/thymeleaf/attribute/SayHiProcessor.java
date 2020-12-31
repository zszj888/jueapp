package com.somecom.thymeleaf.attribute;

import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 根据字典标识生成下拉列表
 *
 * @author Sam
 * @date 2018/8/14
 */
public class SayHiProcessor extends AbstractProcessor {

    private static final String ATTR_NAME = "sayto";
    private static final int PRECEDENCE = 10000;

    public SayHiProcessor() {
        super(TemplateMode.HTML, PRECEDENCE);
    }
    //    public SayHiProcessor(final String dialectPrefix) {
//        super(
//                TemplateMode.HTML, // This processor will apply only to HTML mode
//                dialectPrefix,     // Prefix to be applied to name for matching
//                null,              // No tag name: match any tag name
//                false,             // No prefix to be applied to tag name
//                ATTR_NAME,         // Name of the attribute that will be matched
//                true,              // Apply dialect prefix to attribute name
//                PRECEDENCE,        // Precedence (inside dialect's precedence)
//                true);             // Remove the matched attribute afterwards
//    }


//    protected void doProcess(
//            final ITemplateContext context, final IProcessableElementTag tag,
//            final AttributeName attributeName, final String attributeValue,
//            final IElementTagStructureHandler structureHandler) {
//        System.out.println(tag.getElementCompleteName());
//        structureHandler.setBody(
//                "Hello, " + HtmlEscape.escapeHtml5(attributeValue) + "!", false);
//
//    }
}
