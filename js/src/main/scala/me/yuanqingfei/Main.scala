import scala.scalajs.js.annotation._
import scala.scalajs.js.JSApp

import com.thoughtworks.binding.Binding
import com.thoughtworks.binding.Binding._
import com.thoughtworks.binding.dom

import org.scalajs.dom.raw._
import org.scalajs.dom.html._
import org.scalajs.dom.document

@JSExportTopLevel("Main")
object Main{
    case class Contact(name: Var[String], email: Var[String])

    @dom
    def bindingButton(contact: Contact): Binding[Button] = {
        <button
            onclick={ event: Event =>
            contact.name.value = "Modified Name"
            }
        >
        Modify the name
        </button>
    }

    @dom
    def bindingTr(contact: Contact): Binding[TableRow] = {
        <tr>
            <td>{ contact.name.bind }</td>
            <td>{ contact.email.bind }</td>
            <td>{ bindingButton(contact).bind }</td>
        </tr>
    }

    @dom
    def bindingTable(contacts: BindingSeq[Contact]): Binding[Table] = {
        <table>
            <tbody>
            {
                for (contact <- contacts) yield {
                bindingTr(contact).bind
                }
            }
            </tbody>
        </table>
    }

    @JSExport def main(container: Node) = {
        val data = Vars(Contact(Var("Yang Bo"), Var("yang.bo@rea-group.com")))
        dom.render(container, bindingTable(data))
    }

}