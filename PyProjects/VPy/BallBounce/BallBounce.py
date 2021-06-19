#!/usr/bin/env python
# coding: utf-8

# In[1]:


from vpython import *


# In[2]:


ball = sphere(pos= vector(0, 10, 0), radius= 1, color=color.red)
floor = box(pos=vector(0,0,0), size= vector(10, 0.5, 10), color= color.green)


# In[3]:


ball.velocity= vector(0,0,0)


# In[4]:


dt = 0.01
# time
t=0
# gravity
g = -9.8


# In[5]:


while(t < 20):
    rate(75)
    # velocity = v₀ + gt
    ball.velocity.y = ball.velocity.y + g * dt
    ball.pos = ball.pos + ball.velocity *dt
    # when we change direction (as well as bouncing at edge of ball instead of clipping)
    if ball.pos.y < floor.pos.y + floor.size.y + ball.radius:
        ball.velocity.y = -ball.velocity.y
    t = t + dt
    


# In[ ]:




