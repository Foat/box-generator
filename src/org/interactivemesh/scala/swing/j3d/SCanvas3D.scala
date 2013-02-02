package org.interactivemesh.scala.swing.j3d

/*

License (following the Scala license)

Copyright (c) 2010-2011
August Lammersdorf, InteractiveMesh e.K.
Kolomanstrasse 2a, 85737 Ismaning, Germany / Munich Area
www.InteractiveMesh.com/org
 
All rights reserved.

Permission to use, copy, modify, and distribute this software in source
or binary form for any purpose with or without fee is hereby granted,
provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.

 3. Neither the name of the copyright holder nor the names of its contributors
    may be used to endorse or promote products derived from this
    software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

*/

// Java
import java.awt.{BorderLayout, Dimension}
import java.awt.event.{KeyListener, MouseListener, MouseMotionListener, MouseWheelListener}
import javax.swing.JPanel
// Java 3D
import javax.media.j3d.Canvas3D
// Scala Swing
import scala.swing.{Component, LazyPublisher, Publisher}
import scala.swing.event._

/** The class SCanvas3D provides a heavyweight Scala Swing component 
 *  that Java 3D can render into.
 *  
 *  @author August Lammersdorf, InteractiveMesh
 *  @version 1.3 - 2011/09/19
 */
class SCanvas3D(private val c: Canvas3D) extends Component {
	
  override lazy val peer: JPanel = new JPanel(new BorderLayout) { // with SuperMixin 	
	c.setMinimumSize(new Dimension(0, 0)) // make it resizable
	add(c, BorderLayout.CENTER)
  }
  
  override def toString = "scala.swing Canvas3D wrapper " + c.toString
  
  //
  // Canvas3D API
  //
  
  /** Retrieves the state of the Java 3D renderer for the wrapped Canvas3D instance. 
   */
  def isRendererRunning: Boolean = c.isRendererRunning
  
  /** Starts the Java 3D renderer on the wrapped Canvas3D instance.
   */
  def startRenderer: Unit = c.startRenderer
  
  /** Stops the Java 3D renderer on the wrapped Canvas3D instance.
   */
  def stopRenderer: Unit = c.stopRenderer
   
  
  // Missing in UIElement ?
  def bounds_=(bnd: (Int, Int, Int, Int)): Unit = peer.setBounds(bnd._1, bnd._2, bnd._3, bnd._4)
  def location_=(loc: (Int, Int)): Unit = peer.setLocation(loc._1, loc._2)
		  
  override def focusable: Boolean = c.isFocusable
  override def focusable_=(b: Boolean) = c.setFocusable(b)
  override def requestFocus() = c.requestFocus()
  override def requestFocusInWindow() = c.requestFocusInWindow()
  override def hasFocus: Boolean = c.isFocusOwner
  
  // Canvas3D InputEvents
  
  /** Publishes mouse events (clicks, presses and releases) 
   *  of the wrapped heavyweight Canvas3D instance.
   */
  object canvasMouseClick extends Publisher {
    c.addMouseListener(new MouseListener {
      def mouseEntered(e: java.awt.event.MouseEvent) { }
      def mouseExited(e: java.awt.event.MouseEvent) { }
      def mouseClicked(e: java.awt.event.MouseEvent) { 
        e.setSource(peer)
        publish(new MouseClicked(e))
      }
      def mousePressed(e: java.awt.event.MouseEvent) {
        e.setSource(peer)
        publish(new MousePressed(e))
      }
      def mouseReleased(e: java.awt.event.MouseEvent) { 
        e.setSource(peer)
        publish(new MouseReleased(e))
      }
    })
  }
  /** Publishes mouse events (enters, exits, moves, and drags)
   *  of the wrapped heavyweight Canvas3D instance.
   */
  object canvasMouseMove extends Publisher {
    c.addMouseListener(new MouseListener {
      def mouseEntered(e: java.awt.event.MouseEvent) { 
        e.setSource(peer)
        publish(new MouseEntered(e))
      }
      def mouseExited(e: java.awt.event.MouseEvent) {
        e.setSource(peer)
        publish(new MouseExited(e))
      }
      def mouseClicked(e: java.awt.event.MouseEvent) {}
      def mousePressed(e: java.awt.event.MouseEvent) { }
      def mouseReleased(e: java.awt.event.MouseEvent) { }
    })
    c.addMouseMotionListener(new MouseMotionListener {
      def mouseMoved(e: java.awt.event.MouseEvent) { 
        e.setSource(peer)
        publish(new MouseMoved(e))
      }
      def mouseDragged(e: java.awt.event.MouseEvent) { 
        e.setSource(peer)
        publish(new MouseDragged(e))
      }
    })
  }
  /** Publishes mouse wheel events (enters, exits, moves, and drags)
   *  of the wrapped heavyweight Canvas3D instance.
   */
  object canvasMouseWheel extends Publisher {
  }  
    /*
     * Publishes mouse wheel moves. TODO
     *
    val wheel: Publisher = new LazyPublisher {
      // We need to subscribe lazily and unsubscribe, since components in scroll panes capture 
      // mouse wheel events if there is a listener installed. See ticket #1442.
      lazy val l = new MouseWheelListener {
          def mouseWheelMoved(e: java.awt.event.MouseWheelEvent) { 
            publish(new MouseWheelMoved(e)) }
        }
      def onFirstSubscribe() = peer.addMouseWheelListener(l)
      def onLastUnsubscribe() = peer.removeMouseWheelListener(l)
    }
    */
  
  /** Publishes key events of the wrapped heavyweight Canvas3D instance.
   */
  object canvasKeys extends Publisher {
    c.addKeyListener(new KeyListener {
      def keyPressed(e: java.awt.event.KeyEvent) { 
        e.setSource(peer)
        publish(new KeyPressed(e)) 
      }
      def keyReleased(e: java.awt.event.KeyEvent) { 
        e.setSource(peer)
        publish(new KeyReleased(e))
      }
      def keyTyped(e: java.awt.event.KeyEvent) {
        e.setSource(peer)
        publish(new KeyTyped(e))
      }
    })
  }
  
  /** Publishes focus events of the wrapped heavyweight Canvas3D instance.
   */
  object canvasFocus extends Publisher {   
    c.addFocusListener(new java.awt.event.FocusListener {
      def other(e: java.awt.event.FocusEvent) = e.getOppositeComponent match {
        case c: javax.swing.JComponent => Some(c.getClientProperty("scala.swingWrapper").asInstanceOf[Component])
        case _ => None
      }
    
      def focusGained(e: java.awt.event.FocusEvent) { 
    	val option: Option[Component] = other(e)
    	e.setSource(peer)
        publish(FocusGained(SCanvas3D.this, option, e.isTemporary)) 
      }
      def focusLost(e: java.awt.event.FocusEvent) {
    	val option: Option[Component] = other(e)
    	e.setSource(peer)
        publish(FocusLost(SCanvas3D.this, option, e.isTemporary)) 
      }
    }) 
  }
  
}
