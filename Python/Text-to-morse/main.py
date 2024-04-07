from tkinter import *
from docs.morse_code import morse_code_dict

window = Tk()
window.title("Text to morse code convertor")
window.minsize(width=700, height=300)
window.config(padx=70, pady=70)


def button_click():
    msg = input_msg.get()
    morse_text.config(text=" ".join([morse_code_dict[c] for c in msg.upper()]))
    morse_text.update()


label = Label(text="Enter your message to convert")
label.grid(column=0, row=0)
label.config(width=30, fg="blue")

morse = Label(text="Morse code")
morse.grid(column=1, row=0)
morse.config(fg='red')

input_msg = Entry(width=50)
input_msg.grid(column=0, row=1)
input_msg.lift()

morse_text = Label(text="Morse code will appear here.", width=50)
morse_text.grid(column=1, row=1)

submit_button = Button(text="Convert", command=button_click, border=2, bg="beige")
submit_button.grid(column=0, row=2, columnspan=2)
submit_button.config(width=94)


window.mainloop()
