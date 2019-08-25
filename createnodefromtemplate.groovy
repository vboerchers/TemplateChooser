/* Template Chooser Script
A modification of the original script by Quinbus Flestrin, December 12, 2011

Modified by Allan at ADXSoft 20th Jan 2017
Purpose is to locate and copy pre-defined Templates as new branches to the selected node
The script uses a GUI swingbuilder combo box to capture user input as to which template
and then searches the map for any nodes with node text that commences with the string "Template:""
(these templates can be anywhere in the map).

Version 0.1

History
0.1 Original script

*/

import groovy.swing.SwingBuilder
import java.awt.FlowLayout as FL
import javax.swing.BoxLayout as BXL
import javax.swing.JFrame
import javax.swing.JOptionPane

template_prefix="Template:"

// scan map for Templates
templateList = node.map.root.find{ it.text.startsWith(template_prefix) }.collect{
    it.text.replaceFirst("^${template_prefix}\\s*", '')
}


def s = new SwingBuilder()
s.setVariable('myDialog-properties',[:])
def vars = s.variables
def dial = s.dialog(title:'Template Picker', id:'myDialog', minimumSize: [300,50], modal:true, locationRelativeTo:ui.frame, owner:ui.frame, defaultCloseOperation:JFrame.DISPOSE_ON_CLOSE, pack:true, show:true) {
    panel() {

        boxLayout(axis:BXL.Y_AXIS)

        panel(alignmentX:0f) {
            flowLayout(alignment:FL.LEFT)
            label('Choose Template')
            comboBox(id:'type', items:templateList)
        }

        panel(alignmentX:0f) {
            flowLayout(alignment:FL.RIGHT)
            button(action: action(name: 'OK', defaultButton: true, mnemonic: 'O',
                    closure: {vars.ok = true; dispose()}))
            button(action: action(name: 'Cancel', mnemonic: 'C', closure: {dispose()}))
        }
    }
}

if (vars.ok){
    c.find{
        it.text.matches(template_prefix + '\\s*' + vars.type.selectedItem)
    }.each {
        def copy = node.appendBranch(it)
        type = vars.type.selectedItem
        copy.text = type
        // create attributes in the new node (comment these lines out if not required)
        copy.attributes.set("type", type)
        copy.attributes.set("created", textUtils.defaultDateFormat.format(new Date()))
        // end of attributes creation
    }
}
//JOptionPane.showMessageDialog(ui.frame, 'REMINDER: Dates can be quickly inserted in each node with the Insert Date feature in property panel', "Template Picker" , JOptionPane.INFORMATION_MESSAGE);
