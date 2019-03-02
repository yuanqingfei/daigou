package me.yuanqingfei.daigou

import com.thoughtworks.binding.{Binding, Route, dom}
import com.thoughtworks.binding.Binding.{BindingSeq, Var, Vars}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.{Event, KeyboardEvent, window}
import org.scalajs.dom.ext.{KeyCode, LocalStorage}
import org.scalajs.dom.raw.{HTMLInputElement, Node}

import org.scalajs.dom.html.{Table, TableRow}

import upickle.default.{read, write}
import upickle.default.{ReadWriter => RW, macroRW}

import java.util.Date

@JSExportTopLevel("Main")
object Main{

    case class Order(date: String, client: String, goods: String, completed: Boolean,
        sellPrice: Double, goodsPrice: Double, transferPrice: Double, 
        profit: Double, profitPercent: Double)
    object Order{
        implicit val rw: RW[Order] = macroRW
        // def apply(date: Date, client: String, goods: String, status: Boolean, 
        //     sellPrice:Double, goodsPrice:Double, transferPrice:Double, 
        //     profit: Double, profitPercent: Double) = new Order(date, client, goods, status,
        //     sellPrice, goodsPrice, transferPrice, profit, profitPercent)

        // def unapply(order: Order) = Option((order.date, order.client, order.goods,
        //     order.status, order.sellPrice, order.goodsPrice, order.transferPrice,
        //     order.profit, order.profitPercent))
    }

    Vars 
    
    final case class OrderList(text: String, hash: String, items: BindingSeq[Order])
    // @dom
    // def bindingButton(contact: Contact): Binding[Button] = {
    //     <button
    //         onclick={ event: Event =>
    //         contact.name.value = "Modified Name"
    //         }
    //     >
    //     Modify the name
    //     </button>
    // }

    object Models {
        val LocalStorageName = "daigou-orders"
        def load() = LocalStorage(LocalStorageName).toSeq.flatMap(read[Seq[Order]])
        def save(orders: Seq[Order]) = LocalStorage(LocalStorageName) = write(orders)

        val allOrders = Vars[Order](load(): _*)

        val autoSave: Binding[Unit] = Binding { save(allOrders.all.bind) }
        autoSave.watch()

        val editingOrder = Var[Option[Order]](None)
        val all = OrderList("All", "#/", allOrders)
        val active = OrderList("Active", "#/active", for (order <- allOrders if !order.completed) yield order)
        val completed = OrderList("Completed", "#/completed", for (order <- allOrders if order.completed) yield order)
        val orderLists = Vector(all, active, completed)

        val route = Route.Hash(all)(new Route.Format[OrderList] {
            override def unapply(hashText: String) = orderLists.find(_.hash == window.location.hash)
            override def apply(state: OrderList): String = state.hash
        })
        route.watch()
    }

    import Models._

    @dom
    def tableHeader(): Binding[TableRow] = {
        <tr>
            <th>订单日期</th>
            <th>客户名称</th>
            <th>商品名称</th>
            <th>状态</th>
            <th>商品售价</th>
            <th>商品成本</th>
            <th>快递费</th>
            <th>利润</th>
            <th>利润率</th>
        </tr>
    }

    @dom
    def bindingTr(order: Order): Binding[TableRow] = {
        <tr>
            <td>
                { order.date }
            </td>
            <td>{ order.client }</td>
            <td>{ order.goods }</td>
            <td>{ if (order.completed) "completed" else "processing" }</td>
            <td>{ order.sellPrice.toString }</td>
            <td>{ order.goodsPrice.toString }</td>
            <td>{ order.transferPrice.toString }</td>
            <td>{ order.profit.toString }</td>
            <td>{ order.profitPercent.toString }</td>
        </tr>
    }

    @dom
    def bindingTable(orders: BindingSeq[Order]): Binding[Table] = {
        <table>
            {tableHeader.bind}
            <tbody>
            {
                for (order <- orders) yield {
                    bindingTr(order).bind
                }
            }
            </tbody>
        </table>
    }

    @JSExport def main(container: Node) = {
        // val data = Vars(Order(Var(new Date), Var("testClient"), Var("testGoods"), Var(120), 
        //     Var(100), Var(10), Var(10), Var(0.1)))
        val data = Vars(Order("2019/03/01", "testClient", "testGoods", true, 120, 
            100, 10, 10, 0.1))
        dom.render(container, bindingTable(data))
    }

}